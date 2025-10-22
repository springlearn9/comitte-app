package com.ls.comitte.service;

import com.ls.comitte.model.request.ComitteMemberMapRequest;
import com.ls.comitte.model.response.ComitteMemberMapResponse;
import com.ls.comitte.model.entity.ComitteMemberMap;
import com.ls.comitte.model.entity.Comitte;
import com.ls.comitte.model.entity.Member;
import com.ls.comitte.repository.ComitteMemberMapRepository;
import com.ls.comitte.repository.ComitteRepository;
import com.ls.comitte.repository.MemberRepository;
import com.ls.comitte.util.ResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class ComitteMemberMapService {
    private final ResponseMapper mapper = ResponseMapper.INSTANCE;
    private static final String COMITTE_MEMBER_MAPPING_NOT_FOUND = "Comitte and member mapping not found";
    private final ComitteMemberMapRepository repo;
    private final ComitteRepository comitteRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ComitteMemberMapResponse create(ComitteMemberMapRequest comitteMemberMapRequest) {
        ComitteMemberMap comitteMemberMap = mapper.toEntity(comitteMemberMapRequest);
        
        // Set the relationships from the request IDs
        Comitte comitte = comitteRepository.findById(comitteMemberMapRequest.getComitteId())
                .orElseThrow(() -> new RuntimeException("Comitte not found with ID: " + comitteMemberMapRequest.getComitteId()));
        Member member = memberRepository.findById(comitteMemberMapRequest.getMemberId())
                .orElseThrow(() -> new RuntimeException("Member not found with ID: " + comitteMemberMapRequest.getMemberId()));
        
        comitteMemberMap.setComitte(comitte);
        comitteMemberMap.setMember(member);
        comitteMemberMap.setShareCount(comitteMemberMapRequest.getShareCount());
        
        repo.save(comitteMemberMap);
        return mapper.toResponse(comitteMemberMap);
    }

    @Transactional
    public ComitteMemberMapResponse update(Long id, ComitteMemberMapRequest comitteMemberMapRequest) {
        ComitteMemberMap comitteMemberMap = repo.findById(id)
                .orElseThrow(() -> new RuntimeException(COMITTE_MEMBER_MAPPING_NOT_FOUND));
        
        // Update relationships if they changed
        if (!comitteMemberMap.getComitte().getComitteId().equals(comitteMemberMapRequest.getComitteId())) {
            Comitte comitte = comitteRepository.findById(comitteMemberMapRequest.getComitteId())
                    .orElseThrow(() -> new RuntimeException("Comitte not found with ID: " + comitteMemberMapRequest.getComitteId()));
            comitteMemberMap.setComitte(comitte);
        }
        
        if (!comitteMemberMap.getMember().getMemberId().equals(comitteMemberMapRequest.getMemberId())) {
            Member member = memberRepository.findById(comitteMemberMapRequest.getMemberId())
                    .orElseThrow(() -> new RuntimeException("Member not found with ID: " + comitteMemberMapRequest.getMemberId()));
            comitteMemberMap.setMember(member);
        }
        
        comitteMemberMap.setShareCount(comitteMemberMapRequest.getShareCount());
        repo.save(comitteMemberMap);
        return mapper.toResponse(comitteMemberMap);
    }

    public ComitteMemberMapResponse get(Long id) {
        return repo.findById(id).map(mapper::toResponse)
                .orElseThrow(() -> new RuntimeException(COMITTE_MEMBER_MAPPING_NOT_FOUND));
    }

    @Transactional
    public void delete(Long id) {
        repo.deleteById(id);
    }

}
