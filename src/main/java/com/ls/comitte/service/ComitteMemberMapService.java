package com.ls.comitte.service;

import com.ls.comitte.model.request.ComitteMemberMapRequest;
import com.ls.comitte.model.response.ComitteMemberMapResponse;
import com.ls.comitte.model.entity.ComitteMemberMap;
import com.ls.comitte.repository.ComitteMemberMapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class ComitteMemberMapService {
    private final ComitteMemberMapRepository repo;

    @Transactional
    public ComitteMemberMapResponse create(ComitteMemberMapRequest dto) {
        ComitteMemberMap m = ComitteMemberMap.builder().comitteId(dto.getComitteId()).memberId(dto.getMemberId()).shareCount(dto.getShareCount()).build();
        repo.save(m);
        return toDto(m);
    }

    @Transactional
    public ComitteMemberMapResponse update(Long id, ComitteMemberMapRequest dto) {
        ComitteMemberMap m = repo.findById(id).orElseThrow(() -> new RuntimeException("not found"));
        m.setShareCount(dto.getShareCount());
        repo.save(m);
        return toDto(m);
    }

    public ComitteMemberMapResponse get(Long id) {
        return repo.findById(id).map(this::toDto).orElseThrow(() -> new RuntimeException("not found"));
    }

    @Transactional
    public void delete(Long id) {
        repo.deleteById(id);
    }

    private ComitteMemberMapResponse toDto(ComitteMemberMap m) {
        ComitteMemberMapResponse d = new ComitteMemberMapResponse();
        d.setId(m.getId());
        d.setComitteId(m.getComitteId());
        d.setMemberId(m.getMemberId());
        d.setShareCount(m.getShareCount());
        d.setCreatedTimestamp(m.getCreatedTimestamp());
        d.setUpdatedTimestamp(m.getUpdatedTimestamp());
        return d;
    }
}
