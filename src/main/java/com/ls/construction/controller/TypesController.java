package com.ls.construction.controller;

import com.ls.construction.model.request.TypesRequest;
import com.ls.construction.model.response.TypesResponse;
import com.ls.construction.service.TypesService;
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
 * REST controller for managing Types resources.
 * 
 * <p>This controller provides CRUD operations for type definitions used across modules.</p>
 */
@RestController
@RequestMapping("/api/types")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Types", description = "Type management APIs")
@SecurityRequirement(name = "bearerAuth")
public class TypesController {
    private final TypesService typesService;

    @PostMapping
    @Operation(summary = "Create a new type", description = "Creates a new type definition")
    public ResponseEntity<TypesResponse> create(@Valid @RequestBody TypesRequest dto) {
        log.info("Creating type");
        TypesResponse response = typesService.create(dto);
        log.info("Type created with ID: {}", response.id());
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get type by ID", description = "Retrieves a specific type by its ID")
    public ResponseEntity<TypesResponse> get(@PathVariable Long id) {
        log.info("Getting type with ID: {}", id);
        return ResponseEntity.ok(typesService.get(id));
    }

    @GetMapping
    @Operation(summary = "Get all types", description = "Retrieves all type definitions")
    public ResponseEntity<List<TypesResponse>> getAll() {
        log.info("Getting all types");
        return ResponseEntity.ok(typesService.getAll());
    }

    @GetMapping("/module/{moduleName}")
    @Operation(summary = "Get types by module", description = "Retrieves all types for a specific module")
    public ResponseEntity<List<TypesResponse>> getByModule(@PathVariable String moduleName) {
        log.info("Getting types for module: {}", moduleName);
        return ResponseEntity.ok(typesService.getByModule(moduleName));
    }

    @GetMapping("/module/{moduleName}/category/{category}")
    @Operation(summary = "Get types by module and category", description = "Retrieves all types for a specific module and category")
    public ResponseEntity<List<TypesResponse>> getByModuleAndCategory(@PathVariable String moduleName, @PathVariable String category) {
        log.info("Getting types for module: {} and category: {}", moduleName, category);
        return ResponseEntity.ok(typesService.getByModuleAndCategory(moduleName, category));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update type", description = "Updates an existing type definition")
    public ResponseEntity<TypesResponse> update(@PathVariable Long id, @Valid @RequestBody TypesRequest dto) {
        log.info("Updating type with ID: {}", id);
        return ResponseEntity.ok(typesService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete type", description = "Deletes a type definition by ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Deleting type with ID: {}", id);
        typesService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
