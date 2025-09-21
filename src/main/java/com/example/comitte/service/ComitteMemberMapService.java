package com.example.comitte.service;

import com.example.comitte.dto.map.ComitteMemberMapCreateDto;
import com.example.comitte.dto.map.ComitteMemberMapDto;
import com.example.comitte.entity.ComitteMemberMap;
import com.example.comitte.repository.ComitteMemberMapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class ComitteMemberMapService {
    private final ComitteMemberMapRepository repo;

    @Transactional
    public ComitteMemberMapDto create(ComitteMemberMapCreateDto dto) {
        ComitteMemberMap m = ComitteMemberMap.builder().comitteId(dto.getComitteId()).memberId(dto.getMemberId()).shareCount(dto.getShareCount()).build();
        repo.save(m);
        return toDto(m);
    }

    @Transactional
    public ComitteMemberMapDto update(Long id, ComitteMemberMapCreateDto dto) {
        ComitteMemberMap m = repo.findById(id).orElseThrow(() -> new RuntimeException("not found"));
        m.setShareCount(dto.getShareCount());
        repo.save(m);
        return toDto(m);
    }

    public ComitteMemberMapDto get(Long id) {
        return repo.findById(id).map(this::toDto).orElseThrow(() -> new RuntimeException("not found"));
    }

    @Transactional
    public void delete(Long id) {
        repo.deleteById(id);
    }

    private ComitteMemberMapDto toDto(ComitteMemberMap m) {
        ComitteMemberMapDto d = new ComitteMemberMapDto();
        d.setId(m.getId());
        d.setComitteId(m.getComitteId());
        d.setMemberId(m.getMemberId());
        d.setShareCount(m.getShareCount());
        d.setCreatedTimestamp(m.getCreatedTimestamp());
        d.setUpdatedTimestamp(m.getUpdatedTimestamp());
        return d;
    }
}
