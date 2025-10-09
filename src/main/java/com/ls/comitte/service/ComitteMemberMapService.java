package com.ls.comitte.service;

import com.ls.comitte.model.request.ComitteMemberMapRequest;
import com.ls.comitte.model.response.ComitteMemberMapResponse;
import com.ls.comitte.model.entity.ComitteMemberMap;
import com.ls.comitte.repository.ComitteMemberMapRepository;
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

    @Transactional
    public ComitteMemberMapResponse create(ComitteMemberMapRequest comitteMemberMapRequest) {
        ComitteMemberMap comitteMemberMap = mapper.toEntity(comitteMemberMapRequest);
        repo.save(comitteMemberMap);
        return mapper.toResponse(comitteMemberMap);
    }

    @Transactional
    public ComitteMemberMapResponse update(Long id, ComitteMemberMapRequest comitteMemberMapRequest) {
        ComitteMemberMap comitteMemberMap = repo.findById(id)
                .orElseThrow(() -> new RuntimeException(COMITTE_MEMBER_MAPPING_NOT_FOUND));
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
