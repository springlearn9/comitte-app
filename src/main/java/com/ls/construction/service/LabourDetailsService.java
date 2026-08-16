package com.ls.construction.service;

import com.ls.construction.model.entity.LabourDetails;
import com.ls.construction.model.entity.Project;
import com.ls.construction.model.request.LabourDetailsRequest;
import com.ls.construction.model.response.LabourDetailsResponse;
import com.ls.construction.repository.LabourDetailsRepository;
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
public class LabourDetailsService {
    private final ConstructionMapper mapper = ConstructionMapper.INSTANCE;
    private static final String LABOUR_NOT_FOUND = "Labour details not found";
    private static final String PROJECT_NOT_FOUND = "Project not found";

    private final LabourDetailsRepository labourDetailsRepository;
    private final ProjectRepository projectRepository;

    public LabourDetailsResponse get(Long id) {
        LabourDetails labourDetails = labourDetailsRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, LABOUR_NOT_FOUND));
        return mapper.toResponse(labourDetails);
    }

    public List<LabourDetailsResponse> getAll() {
        return labourDetailsRepository.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    public List<LabourDetailsResponse> getByProject(Long projectId) {
        return labourDetailsRepository.findByProjectProjectId(projectId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional
    public LabourDetailsResponse create(LabourDetailsRequest labourDetailsRequest) {
        LabourDetails labourDetails = mapper.toEntity(labourDetailsRequest);
        
        if (labourDetails.getAudit() == null) {
            labourDetails.setAudit(new com.ls.common.model.AuditMetadata());
        }
        
        Project project = projectRepository.findById(labourDetailsRequest.getProjectId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, PROJECT_NOT_FOUND));
        labourDetails.setProject(project);
        
        labourDetailsRepository.save(labourDetails);
        return mapper.toResponse(labourDetails);
    }

    @Transactional
    public LabourDetailsResponse update(Long id, LabourDetailsRequest labourDetailsRequest) {
        LabourDetails labourDetails = labourDetailsRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, LABOUR_NOT_FOUND));
        
        labourDetails.setLabourDate(labourDetailsRequest.getLabourDate());
        labourDetails.setLabourType(labourDetailsRequest.getLabourType());
        labourDetails.setLabourAmount(labourDetailsRequest.getLabourAmount());
        labourDetails.setDetails(labourDetailsRequest.getDetails());
        labourDetails.setTags(labourDetailsRequest.getTags());
        
        labourDetailsRepository.save(labourDetails);
        return mapper.toResponse(labourDetails);
    }

    @Transactional
    public void delete(Long id) {
        if (!labourDetailsRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, LABOUR_NOT_FOUND);
        }
        labourDetailsRepository.deleteById(id);
    }
}
