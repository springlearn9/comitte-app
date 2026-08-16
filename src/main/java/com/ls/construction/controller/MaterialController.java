package com.ls.construction.controller;

import com.ls.construction.model.request.MaterialRequest;
import com.ls.construction.model.response.MaterialResponse;
import com.ls.construction.service.MaterialService;
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
 * REST controller for managing Material resources.
 * 
 * <p>This controller provides CRUD operations for construction materials.</p>
 */
@RestController
@RequestMapping("/api/materials")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Materials", description = "Material management APIs")
@SecurityRequirement(name = "bearerAuth")
public class MaterialController {
    private final MaterialService materialService;

    @PostMapping
    @Operation(summary = "Create a new material entry", description = "Creates a new material entry for a project")
    public ResponseEntity<MaterialResponse> create(@Valid @RequestBody MaterialRequest dto) {
        log.info("Creating material entry");
        MaterialResponse response = materialService.create(dto);
        log.info("Material entry created with ID: {}", response.materialId());
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{materialId}")
    @Operation(summary = "Get material by ID", description = "Retrieves a specific material entry by its ID")
    public ResponseEntity<MaterialResponse> get(@PathVariable Long materialId) {
        log.info("Getting material with ID: {}", materialId);
        return ResponseEntity.ok(materialService.get(materialId));
    }

    @GetMapping
    @Operation(summary = "Get all materials", description = "Retrieves all material entries")
    public ResponseEntity<List<MaterialResponse>> getAll() {
        log.info("Getting all materials");
        return ResponseEntity.ok(materialService.getAll());
    }

    @GetMapping("/project/{projectId}")
    @Operation(summary = "Get materials by project", description = "Retrieves all materials for a specific project")
    public ResponseEntity<List<MaterialResponse>> getByProject(@PathVariable Long projectId) {
        log.info("Getting materials for project ID: {}", projectId);
        return ResponseEntity.ok(materialService.getByProject(projectId));
    }

    @PutMapping("/{materialId}")
    @Operation(summary = "Update material", description = "Updates an existing material entry")
    public ResponseEntity<MaterialResponse> update(@PathVariable Long materialId, @Valid @RequestBody MaterialRequest dto) {
        log.info("Updating material with ID: {}", materialId);
        return ResponseEntity.ok(materialService.update(materialId, dto));
    }

    @DeleteMapping("/{materialId}")
    @Operation(summary = "Delete material", description = "Deletes a material entry by ID")
    public ResponseEntity<Void> delete(@PathVariable Long materialId) {
        log.info("Deleting material with ID: {}", materialId);
        materialService.delete(materialId);
        return ResponseEntity.noContent().build();
    }
}
