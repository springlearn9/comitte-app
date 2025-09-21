package com.example.comitte.service;

import com.example.comitte.dto.comitte.ComitteCreateDto;
import com.example.comitte.dto.comitte.ComitteDto;
import com.example.comitte.entity.Comitte;
import com.example.comitte.entity.ComitteMemberMap;
import com.example.comitte.repository.ComitteMemberMapRepository;
import com.example.comitte.repository.ComitteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ComitteService {
    private final ComitteRepository repo;
    private final ComitteMemberMapRepository mapRepo;

    public Page<ComitteDto> list(Pageable p) {
        var pg = repo.findAll(p);
        List<ComitteDto> l = pg.stream().map(this::toDto).collect(Collectors.toList());
        return new PageImpl<>(l, p, pg.getTotalElements());
    }

    public ComitteDto get(Long id) {
        return repo.findById(id).map(this::toDto).orElseThrow(() -> new RuntimeException("not found"));
    }

    @Transactional
    public ComitteDto create(ComitteCreateDto dto) {
        Comitte c = Comitte.builder().ownerId(dto.getOwnerId()).comitteName(dto.getComitteName()).startDate(dto.getStartDate()).fullAmount(dto.getFullAmount()).membersCount(dto.getMembersCount()).fullShare(dto.getFullShare()).dueDateDays(dto.getDueDateDays()).paymentDateDays(dto.getPaymentDateDays()).build();
        repo.save(c);
        return toDto(c);
    }

    @Transactional
    public ComitteDto update(Long id, ComitteCreateDto dto) {
        Comitte c = repo.findById(id).orElseThrow(() -> new RuntimeException("not found"));
        c.setComitteName(dto.getComitteName());
        c.setStartDate(dto.getStartDate());
        c.setFullAmount(dto.getFullAmount());
        c.setMembersCount(dto.getMembersCount());
        c.setFullShare(dto.getFullShare());
        c.setDueDateDays(dto.getDueDateDays());
        c.setPaymentDateDays(dto.getPaymentDateDays());
        repo.save(c);
        return toDto(c);
    }

    @Transactional
    public void delete(Long id) { // business rule: prevent deletion if bids exist could be added
        repo.deleteById(id);
    }

    @Transactional
    public ComitteDto assignMembers(Long id, java.util.List<Long> memberIds) {
        Comitte c = repo.findById(id).orElseThrow(() -> new RuntimeException("not found")); // clear existing? just add
        for (Long mid : memberIds) {
            ComitteMemberMap m = ComitteMemberMap.builder().comitteId(id).memberId(mid).shareCount(1).build();
            mapRepo.save(m);
        }
        return toDto(c);
    }

    private ComitteDto toDto(Comitte c) {
        ComitteDto d = new ComitteDto();
        d.setComitteId(c.getComitteId());
        d.setOwnerId(c.getOwnerId());
        d.setComitteName(c.getComitteName());
        d.setStartDate(c.getStartDate());
        d.setFullAmount(c.getFullAmount());
        d.setMembersCount(c.getMembersCount());
        d.setFullShare(c.getFullShare());
        d.setDueDateDays(c.getDueDateDays());
        d.setPaymentDateDays(c.getPaymentDateDays());
        d.setCreatedTimestamp(c.getCreatedTimestamp());
        d.setUpdatedTimestamp(c.getUpdatedTimestamp());
        return d;
    }
}
