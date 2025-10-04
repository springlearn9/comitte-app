package com.ls.comitte.service;

import com.ls.comitte.model.request.ComitteRequest;
import com.ls.comitte.model.response.ComitteResponse;
import com.ls.comitte.model.entity.Comitte;
import com.ls.comitte.model.entity.ComitteMemberMap;
import com.ls.comitte.repository.ComitteMemberMapRepository;
import com.ls.comitte.repository.ComitteRepository;
import com.ls.comitte.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ComitteService {
    private final ComitteRepository comitteRepository;
    private final MemberRepository memberRepository;
    private final ComitteMemberMapRepository comitteMemberMapRepository;
    public ComitteResponse get(Long comitteId) {
        return comitteRepository.findById(comitteId).map(this::toDto).orElseThrow(() -> new RuntimeException("not found"));
    }

    @Transactional
    public ComitteResponse create(ComitteRequest dto) {
        Comitte c = Comitte.builder().ownerId(dto.getOwnerId()).comitteName(dto.getComitteName()).startDate(dto.getStartDate()).fullAmount(dto.getFullAmount()).membersCount(dto.getMembersCount()).fullShare(dto.getFullShare()).dueDateDays(dto.getDueDateDays()).paymentDateDays(dto.getPaymentDateDays()).build();
        comitteRepository.save(c);
        return toDto(c);
    }

    @Transactional
    public ComitteResponse update(Long comitteId, ComitteRequest dto) {
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
    public ComitteResponse assignMembers(Long id, java.util.List<Long> memberIds) {
        Comitte c = comitteRepository.findById(id).orElseThrow(() -> new RuntimeException("not found")); // clear existing? just add
        for (Long mid : memberIds) {
            ComitteMemberMap m = ComitteMemberMap.builder().comitteId(id).memberId(mid).shareCount(1).build();
            comitteMemberMapRepository.save(m);
        }
        return toDto(c);
    }

    public List<ComitteResponse> getMemberComittes(Long memberId) {
        return comitteRepository.findComittesByMemberId(memberId)
                .stream()
                .map(this::toDto) // convert to DTO
                .toList();
    }

    private ComitteResponse toDto(Comitte c) {
        ComitteResponse d = new ComitteResponse();
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
