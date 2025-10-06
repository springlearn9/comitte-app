package com.ls.comitte.service;

import com.ls.comitte.model.request.ComitteRequest;
import com.ls.comitte.model.response.ComitteResponse;
import com.ls.comitte.model.entity.Comitte;
import com.ls.comitte.model.entity.ComitteMemberMap;
import com.ls.comitte.repository.ComitteMemberMapRepository;
import com.ls.comitte.repository.ComitteRepository;
import com.ls.comitte.repository.MemberRepository;
import com.ls.comitte.util.AppUtil;
import com.ls.comitte.util.ResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ComitteService {
    private final ResponseMapper mapper = ResponseMapper.INSTANCE;
    private static final String COMITTE_NOT_FOUND = "Comitte not found";

    private final ComitteRepository comitteRepository;
    private final MemberRepository memberRepository;
    private final ComitteMemberMapRepository comitteMemberMapRepository;

    public ComitteResponse get(Long comitteId) {
        return comitteRepository.findById(comitteId).map(mapper::toResponse)
                .orElseThrow(() -> new RuntimeException(COMITTE_NOT_FOUND));
    }

    @Transactional
    public ComitteResponse create(ComitteRequest comitteRequest) {
        Comitte comitte = mapper.toEntity(comitteRequest);
        comitteRepository.save(comitte);
        return mapper.toResponse(comitte);
    }

    @Transactional
    public ComitteResponse update(Long comitteId, ComitteRequest comitteRequest) {
        Comitte comitte = comitteRepository.findById(comitteId)
                .orElseThrow(() -> new RuntimeException(COMITTE_NOT_FOUND));
        AppUtil.update(comitte, comitteRequest);
        comitteRepository.save(comitte);
        return mapper.toResponse(comitte);
    }

    @Transactional
    public void delete(Long id) { // business rule: prevent deletion if bids exist could be added
        comitteRepository.deleteById(id);
    }

    @Transactional
    public ComitteResponse assignMembers(Long comitteId, List<Long> memberIds) {
        Comitte comitte = comitteRepository.findById(comitteId)
                .orElseThrow(() -> new RuntimeException(COMITTE_NOT_FOUND));
        for (Long mid : memberIds) {
            ComitteMemberMap comitteMemberMap = ComitteMemberMap.builder().comitteId(comitteId).memberId(mid).shareCount(1).build();
            comitteMemberMapRepository.save(comitteMemberMap);
        }
        return mapper.toResponse(comitte);
    }

    public List<ComitteResponse> getMemberComittes(Long memberId) {
        return comitteRepository.findComittesByMemberId(memberId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

}
