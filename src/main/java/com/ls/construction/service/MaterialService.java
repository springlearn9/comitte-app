package com.ls.construction.service;

import com.ls.construction.model.entity.Material;
import com.ls.construction.model.entity.Project;
import com.ls.construction.model.request.MaterialRequest;
import com.ls.construction.model.response.MaterialResponse;
import com.ls.construction.repository.MaterialRepository;
import com.ls.construction.repository.ProjectRepository;
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
public class MaterialService {
    private final ConstructionMapper mapper = ConstructionMapper.INSTANCE;
    private static final String MATERIAL_NOT_FOUND = "Material not found";
    private static final String PROJECT_NOT_FOUND = "Project not found";

    private final MaterialRepository materialRepository;
    private final ProjectRepository projectRepository;

    public MaterialResponse get(Long materialId) {
        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, MATERIAL_NOT_FOUND));
        return mapper.toResponse(material);
    }

    public List<MaterialResponse> getAll() {
        return materialRepository.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    public List<MaterialResponse> getByProject(Long projectId) {
        return materialRepository.findByProjectProjectId(projectId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional
    public MaterialResponse create(MaterialRequest materialRequest) {
        Material material = mapper.toEntity(materialRequest);
        
        if (material.getAudit() == null) {
            material.setAudit(new com.ls.common.model.AuditMetadata());
        }
        
        Project project = projectRepository.findById(materialRequest.getProjectId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, PROJECT_NOT_FOUND));
        material.setProject(project);
        
        materialRepository.save(material);
        return mapper.toResponse(material);
    }

    @Transactional
    public MaterialResponse update(Long materialId, MaterialRequest materialRequest) {
        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, MATERIAL_NOT_FOUND));
        
        material.setMaterialDate(materialRequest.getMaterialDate());
        material.setDetails(materialRequest.getDetails());
        material.setMaterial(materialRequest.getMaterial());
        material.setMaterialType(materialRequest.getMaterialType());
        material.setLabour(materialRequest.getLabour());
        material.setLabourType(materialRequest.getLabourType());
        material.setSupplier(materialRequest.getSupplier());
        material.setQuantity(materialRequest.getQuantity());
        material.setUnit(materialRequest.getUnit());
        material.setPricePerUnit(materialRequest.getPricePerUnit());
        material.setAmount(materialRequest.getAmount());
        material.setBhada(materialRequest.getBhada());
        material.setTotalAmount(materialRequest.getTotalAmount());
        material.setTags(materialRequest.getTags());
        material.setPaymentStatus(materialRequest.getPaymentStatus());
        material.setPaidDate(materialRequest.getPaidDate());
        material.setPaymentId(materialRequest.getPaymentId());
        material.setPaymentDetails(materialRequest.getPaymentDetails());
        
        materialRepository.save(material);
        return mapper.toResponse(material);
    }

    @Transactional
    public void delete(Long materialId) {
        if (!materialRepository.existsById(materialId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, MATERIAL_NOT_FOUND);
        }
        materialRepository.deleteById(materialId);
    }
}
