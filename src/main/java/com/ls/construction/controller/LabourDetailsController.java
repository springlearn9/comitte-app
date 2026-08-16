package com.ls.construction.controller;

import com.ls.construction.model.request.LabourDetailsRequest;
import com.ls.construction.model.response.LabourDetailsResponse;
import com.ls.construction.service.LabourDetailsService;
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
 * REST controller for managing Labour Details resources.
 * 
 * <p>This controller provides CRUD operations for construction labour records.</p>
 */
@RestController
@RequestMapping("/api/labour-details")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Labour Details", description = "Labour management APIs")
@SecurityRequirement(name = "bearerAuth")
public class LabourDetailsController {
    private final LabourDetailsService labourDetailsService;

    @PostMapping
    @Operation(summary = "Create a new labour entry", description = "Creates a new labour details entry for a project")
    public ResponseEntity<LabourDetailsResponse> create(@Valid @RequestBody LabourDetailsRequest dto) {
        log.info("Creating labour details entry");
        LabourDetailsResponse response = labourDetailsService.create(dto);
        log.info("Labour details entry created with ID: {}", response.id());
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get labour details by ID", description = "Retrieves a specific labour details entry by its ID")
    public ResponseEntity<LabourDetailsResponse> get(@PathVariable Long id) {
        log.info("Getting labour details with ID: {}", id);
        return ResponseEntity.ok(labourDetailsService.get(id));
    }

    @GetMapping
    @Operation(summary = "Get all labour details", description = "Retrieves all labour details entries")
    public ResponseEntity<List<LabourDetailsResponse>> getAll() {
        log.info("Getting all labour details");
        return ResponseEntity.ok(labourDetailsService.getAll());
    }

    @GetMapping("/project/{projectId}")
    @Operation(summary = "Get labour details by project", description = "Retrieves all labour details for a specific project")
    public ResponseEntity<List<LabourDetailsResponse>> getByProject(@PathVariable Long projectId) {
        log.info("Getting labour details for project ID: {}", projectId);
        return ResponseEntity.ok(labourDetailsService.getByProject(projectId));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update labour details", description = "Updates an existing labour details entry")
    public ResponseEntity<LabourDetailsResponse> update(@PathVariable Long id, @Valid @RequestBody LabourDetailsRequest dto) {
        log.info("Updating labour details with ID: {}", id);
        return ResponseEntity.ok(labourDetailsService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete labour details", description = "Deletes a labour details entry by ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Deleting labour details with ID: {}", id);
        labourDetailsService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
