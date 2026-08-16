package com.ls.construction.service;

import com.ls.construction.model.entity.Types;
import com.ls.construction.model.request.TypesRequest;
import com.ls.construction.model.response.TypesResponse;
import com.ls.construction.repository.TypesRepository;
import com.ls.construction.util.ConstructionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import jakarta.transaction.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TypesService {
    private final ConstructionMapper mapper = ConstructionMapper.INSTANCE;
    private static final String TYPE_NOT_FOUND = "Type not found";

    private final TypesRepository typesRepository;

    public TypesResponse get(Long id) {
        Types types = typesRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, TYPE_NOT_FOUND));
        return mapper.toResponse(types);
    }

    public List<TypesResponse> getAll() {
        return typesRepository.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    public List<TypesResponse> getByModule(String moduleName) {
        return typesRepository.findByModuleName(moduleName).stream()
                .map(mapper::toResponse)
                .toList();
    }

    public List<TypesResponse> getByModuleAndCategory(String moduleName, String category) {
        return typesRepository.findByModuleNameAndCategory(moduleName, category).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional
    public TypesResponse create(TypesRequest typesRequest) {
        Types types = mapper.toEntity(typesRequest);
        
        if (types.getAudit() == null) {
            types.setAudit(new com.ls.common.model.AuditMetadata());
        }
        
        typesRepository.save(types);
        return mapper.toResponse(types);
    }

    @Transactional
    public TypesResponse update(Long id, TypesRequest typesRequest) {
        Types types = typesRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, TYPE_NOT_FOUND));
        
        types.setModuleName(typesRequest.getModuleName());
        types.setCategory(typesRequest.getCategory());
        types.setTypeName(typesRequest.getTypeName());
        
        typesRepository.save(types);
        return mapper.toResponse(types);
    }

    @Transactional
    public void delete(Long id) {
        if (!typesRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, TYPE_NOT_FOUND);
        }
        typesRepository.deleteById(id);
    }
}
