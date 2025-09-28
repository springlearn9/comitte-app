package com.example.comitte.service;

import com.example.comitte.model.dto.comitte.ComitteCreateDto;
import com.example.comitte.model.dto.comitte.ComitteDto;
import com.example.comitte.model.entity.Comitte;
import com.example.comitte.model.entity.ComitteMemberId;
import com.example.comitte.model.entity.ComitteMemberMap;
import com.example.comitte.model.entity.Member;
import com.example.comitte.repository.ComitteRepository;
import com.example.comitte.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ComitteService {
    private final ComitteRepository comitteRepository;
    private final MemberRepository memberRepository;

    public ComitteDto get(Long comitteId) {
        return comitteRepository.findById(comitteId).map(this::toDto).orElseThrow(() -> new RuntimeException("not found"));
    }

    @Transactional
    public ComitteDto create(ComitteCreateDto dto) {
        Comitte c = Comitte.builder().ownerId(dto.getOwnerId()).comitteName(dto.getComitteName()).startDate(dto.getStartDate()).fullAmount(dto.getFullAmount()).membersCount(dto.getMembersCount()).fullShare(dto.getFullShare()).dueDateDays(dto.getDueDateDays()).paymentDateDays(dto.getPaymentDateDays()).build();
        comitteRepository.save(c);
        return toDto(c);
    }

    @Transactional
    public ComitteDto update(Long comitteId, ComitteCreateDto dto) {
        Comitte c = comitteRepository.findById(comitteId).orElseThrow(() -> new RuntimeException("not found"));
        c.setComitteName(dto.getComitteName());
        c.setStartDate(dto.getStartDate());
        c.setFullAmount(dto.getFullAmount());
        c.setMembersCount(dto.getMembersCount());
        c.setFullShare(dto.getFullShare());
        c.setDueDateDays(dto.getDueDateDays());
        c.setPaymentDateDays(dto.getPaymentDateDays());
        comitteRepository.save(c);
        return toDto(c);
    }

    @Transactional
    public void delete(Long id) { // business rule: prevent deletion if bids exist could be added
        comitteRepository.deleteById(id);
    }

    @Transactional
    public ComitteDto assignMembers(Long comitteId, Map<Long, Integer> memberShares) {
        Comitte comitte = comitteRepository.findById(comitteId)
                .orElseThrow(() -> new RuntimeException("Comitte not found"));

        for (Map.Entry<Long, Integer> entry : memberShares.entrySet()) {
            Long memberId = entry.getKey();
            Integer shareCount = entry.getValue();

            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new RuntimeException("Member not found"));

            ComitteMemberMap mapping = ComitteMemberMap.builder()
                    .id(new ComitteMemberId(comitteId, memberId))
                    .comitte(comitte)
                    .member(member)
                    .shareCount(shareCount)
                    .build();

            comitte.getMemberMappings().add(mapping);
        }

        Comitte updated = comitteRepository.save(comitte);
        return toDto(updated);
    }


    public List<ComitteDto> getMemberComittes(Long memberId) {
        return comitteRepository.findComittesByMemberId(memberId)
                .stream()
                .map(this::toDto) // convert to DTO
                .toList();
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
