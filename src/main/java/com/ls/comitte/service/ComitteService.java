package com.ls.comitte.service;

import com.ls.comitte.model.request.ComitteRequest;
import com.ls.comitte.model.response.ComitteResponse;
import com.ls.comitte.model.entity.Comitte;
import com.ls.comitte.model.entity.ComitteMemberMap;
import com.ls.comitte.model.entity.Member;
import com.ls.comitte.model.response.ComitteMemberMapResponse;
import com.ls.comitte.repository.ComitteMemberMapRepository;
import com.ls.comitte.repository.ComitteRepository;
import com.ls.comitte.repository.MemberRepository;
import com.ls.comitte.repository.BidRepository;
import com.ls.comitte.util.ServiceUtil;
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
    private final BidRepository bidRepository;

    public ComitteResponse get(Long comitteId) {
        Comitte comitte = comitteRepository.findById(comitteId)
                .orElseThrow(() -> new RuntimeException(COMITTE_NOT_FOUND));
        return enrichWithBidsCount(mapper.toResponse(comitte));
    }

    @Transactional
    public ComitteResponse create(ComitteRequest comitteRequest) {
        Comitte comitte = mapper.toEntity(comitteRequest);
        
        // Set the owner from ownerId
        Member owner = memberRepository.findById(comitteRequest.getOwnerId())
                .orElseThrow(() -> new RuntimeException("Owner not found with ID: " + comitteRequest.getOwnerId()));
        comitte.setOwner(owner);
        
        comitteRepository.save(comitte);
        return enrichWithBidsCount(mapper.toResponse(comitte));
    }

    @Transactional
    public ComitteResponse update(Long comitteId, ComitteRequest comitteRequest) {
        Comitte comitte = comitteRepository.findById(comitteId)
                .orElseThrow(() -> new RuntimeException(COMITTE_NOT_FOUND));
        ServiceUtil.update(comitte, comitteRequest);
        comitteRepository.save(comitte);
        return enrichWithBidsCount(mapper.toResponse(comitte));
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
            Member member = memberRepository.findById(mid)
                    .orElseThrow(() -> new RuntimeException("Member not found with ID: " + mid));
            
            ComitteMemberMap comitteMemberMap = ComitteMemberMap.builder()
                    .comitte(comitte)
                    .member(member)
                    .shareCount(1)
                    .build();
            comitteMemberMapRepository.save(comitteMemberMap);
        }
        return enrichWithBidsCount(mapper.toResponse(comitte));
    }

    public List<ComitteResponse> getMemberComittes(Long memberId) {
        List<ComitteResponse> responses = comitteRepository.findComittesByMemberId(memberId)
                .stream()
                .map(mapper::toResponse)
                .toList();
        return enrichListWithBidsCount(responses);
    }

    public List<ComitteResponse> getOwnerComittes(Long ownerId) {
        List<ComitteResponse> responses = comitteRepository.findComittesByOwnerId(ownerId)
                .stream()
                .map(mapper::toResponse)
                .toList();
        return enrichListWithBidsCount(responses);
    }

    public List<ComitteMemberMapResponse> getAllAssociatedMembers(Long comitteId) {
        return comitteMemberMapRepository.findByComitte_ComitteId(comitteId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    /**
     * Helper method to enrich ComitteResponse with bids count
     */
    private ComitteResponse enrichWithBidsCount(ComitteResponse response) {
        Long bidsCount = bidRepository.countByComitte_ComitteId(response.comitteId());
        return new ComitteResponse(
            response.comitteId(),
            response.ownerId(),
            response.ownerName(),
            response.comitteName(),
            response.startDate(),
            response.fullAmount(),
            response.membersCount(),
            response.fullShare(),
            response.dueDateDays(),
            response.paymentDateDays(),
            bidsCount.intValue(),
            response.createdTimestamp(),
            response.updatedTimestamp()
        );
    }

    /**
     * Helper method to enrich list of ComitteResponse with bids count
     */
    private List<ComitteResponse> enrichListWithBidsCount(List<ComitteResponse> responses) {
        return responses.stream()
                .map(this::enrichWithBidsCount)
                .toList();
    }

}
