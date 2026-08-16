package com.ls.construction.controller;

import com.ls.construction.model.request.ProjectRequest;
import com.ls.construction.model.response.ProjectResponse;
import com.ls.construction.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

/**
 * REST controller for managing Project resources.
 * 
 * <p>This controller provides CRUD operations for construction projects.</p>
 */
@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Projects", description = "Project management APIs")
@SecurityRequirement(name = "bearerAuth")
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping
    @Operation(summary = "Create a new project", description = "Creates a new construction project")
    public ResponseEntity<ProjectResponse> create(@Valid @RequestBody ProjectRequest dto) {
        log.info("Creating project");
        ProjectResponse response = projectService.create(dto);
        log.info("Project created with ID: {}", response.projectId());
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{projectId}")
    @Operation(summary = "Get project by ID", description = "Retrieves a specific project by its ID")
    public ResponseEntity<ProjectResponse> get(@PathVariable Long projectId) {
        log.info("Getting project with ID: {}", projectId);
        return ResponseEntity.ok(projectService.get(projectId));
    }

    @GetMapping
    @Operation(summary = "Get all projects", description = "Retrieves all projects")
    public ResponseEntity<List<ProjectResponse>> getAll() {
        log.info("Getting all projects");
        return ResponseEntity.ok(projectService.getAll());
    }

    @PutMapping("/{projectId}")
    @Operation(summary = "Update project", description = "Updates an existing project")
    public ResponseEntity<ProjectResponse> update(@PathVariable Long projectId, @Valid @RequestBody ProjectRequest dto) {
        log.info("Updating project with ID: {}", projectId);
        return ResponseEntity.ok(projectService.update(projectId, dto));
    }

    @DeleteMapping("/{projectId}")
    @Operation(summary = "Delete project", description = "Deletes a project by ID")
    public ResponseEntity<Void> delete(@PathVariable Long projectId) {
        log.info("Deleting project with ID: {}", projectId);
        projectService.delete(projectId);
        return ResponseEntity.noContent().build();
    }
}
