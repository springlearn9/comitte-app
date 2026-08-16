package com.ls.construction.service;

import com.ls.construction.model.entity.Project;
import com.ls.construction.model.request.ProjectRequest;
import com.ls.construction.model.response.ProjectResponse;
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
public class ProjectService {
    private final ConstructionMapper mapper = ConstructionMapper.INSTANCE;
    private static final String PROJECT_NOT_FOUND = "Project not found";

    private final ProjectRepository projectRepository;

    public ProjectResponse get(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, PROJECT_NOT_FOUND));
        return mapper.toResponse(project);
    }

    public List<ProjectResponse> getAll() {
        return projectRepository.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional
    public ProjectResponse create(ProjectRequest projectRequest) {
        Project project = mapper.toEntity(projectRequest);
        
        if (project.getAudit() == null) {
            project.setAudit(new com.ls.common.model.AuditMetadata());
        }
        
        projectRepository.save(project);
        return mapper.toResponse(project);
    }

    @Transactional
    public ProjectResponse update(Long projectId, ProjectRequest projectRequest) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, PROJECT_NOT_FOUND));
        
        project.setName(projectRequest.getName());
        project.setDetails(projectRequest.getDetails());
        
        projectRepository.save(project);
        return mapper.toResponse(project);
    }

    @Transactional
    public void delete(Long projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, PROJECT_NOT_FOUND);
        }
        projectRepository.deleteById(projectId);
    }
}
