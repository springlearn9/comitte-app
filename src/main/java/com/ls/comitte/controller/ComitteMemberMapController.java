package com.ls.comitte.controller;

import com.ls.comitte.model.request.ComitteMemberMapRequest;
import com.ls.comitte.model.response.ComitteMemberMapResponse;
import com.ls.comitte.service.ComitteMemberMapService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

/**
 * REST controller for managing committee-member mappings.
 * 
 * Provides endpoints for creating, retrieving, updating, and deleting mappings
 * between committees and members. These mappings represent member participation
 * in committees with associated metadata.
 * All endpoints require appropriate authentication and authorization.
 * 
 * Base Path: /api/comitte-member-map
 */
@RestController
@RequestMapping("/api/comitte-member-map")
@RequiredArgsConstructor
@Slf4j
public class ComitteMemberMapController {
    private final ComitteMemberMapService service;

    /**
     * Creates a new committee-member mapping.
     * 
     * Purpose: Establishes a mapping between a committee and a member
     * Request: ComitteMemberMapRequest (JSON body) - validated
     * Response: ComitteMemberMapResponse with created mapping details
     * Status Codes:
     *   - 201: Mapping created successfully
     *   - 400: Invalid input data (validation failure, duplicate mapping)
     *   - 404: Committee or member not found
     *   - 500: Server error
     * 
     * Validation: Enforced via @Valid annotation on ComitteMemberMapRequest
     * Security: Requires authentication and committee owner verification
     * Performance: Single database insert with referential integrity checks
     * Error Mapping: Service validates unique constraint on committee-member pair
     * 
     * @param dto the mapping creation request
     * @return ResponseEntity with created mapping details and 201 status
     */
    @PostMapping
    public ResponseEntity<ComitteMemberMapResponse> create(@Valid @RequestBody ComitteMemberMapRequest dto) {
        log.info("Creating comitte member map with data: {}", dto);
        ComitteMemberMapResponse response = service.create(dto);
        log.info("Comitte member map created with ID: {}", response.id());
        return ResponseEntity.status(201).body(response);
    }

    /**
     * Retrieves a committee-member mapping by ID.
     * 
     * Purpose: Fetches detailed information for a specific committee-member mapping
     * Request: Path variable id
     * Response: ComitteMemberMapResponse with mapping details
     * Status Codes:
     *   - 200: Mapping found and returned
     *   - 404: Mapping not found
     *   - 500: Server error
     * 
     * Security: Requires authentication; may restrict based on committee membership
     * Performance: Single database query by primary key
     * Error Mapping: Service throws ResponseStatusException with 404 if mapping not found
     * 
     * @param id the ID of the mapping to retrieve
     * @return ResponseEntity with mapping details and 200 status
     */
    @GetMapping("/{id}")
    public ResponseEntity<ComitteMemberMapResponse> get(@PathVariable Long id) {
        log.info("Fetching comitte member map for ID: {}", id);
        ComitteMemberMapResponse response = service.get(id);
        log.info("Fetched comitte member map: {}", response);
        return ResponseEntity.ok(response);
    }

    /**
     * Updates an existing committee-member mapping.
     * 
     * Purpose: Updates metadata for a specific committee-member mapping
     * Request: Path variable id and ComitteMemberMapRequest (JSON body) - validated
     * Response: ComitteMemberMapResponse with updated mapping details
     * Status Codes:
     *   - 200: Mapping updated successfully
     *   - 400: Invalid input data (validation failure)
     *   - 404: Mapping not found
     *   - 403: Forbidden (user is not the committee owner)
     *   - 500: Server error
     * 
     * Validation: Enforced via @Valid annotation on ComitteMemberMapRequest
     * Security: Requires authentication and committee owner verification
     * Performance: Single database update operation
     * Error Mapping: Service throws ResponseStatusException with appropriate status
     * Developer Notes: Committee and member IDs are typically immutable; update metadata only
     * 
     * @param id the ID of the mapping to update
     * @param request the updated mapping data
     * @return ResponseEntity with updated mapping details and 200 status
     */
    @PutMapping("/{id}")
    public ResponseEntity<ComitteMemberMapResponse> update(@PathVariable Long id, @Valid @RequestBody ComitteMemberMapRequest request) {
        log.info("Updating comitte member map with ID: {} using data: {}", id, request);
        ComitteMemberMapResponse response = service.update(id, request);
        log.info("Updated comitte member map: {}", response);
        return ResponseEntity.ok(response);
    }

    /**
     * Deletes a committee-member mapping.
     * 
     * Purpose: Removes a member from a committee by deleting the mapping
     * Request: Path variable id
     * Response: No content
     * Status Codes:
     *   - 204: Mapping deleted successfully
     *   - 404: Mapping not found
     *   - 403: Forbidden (user is not the committee owner)
     *   - 500: Server error
     * 
     * Security: Requires authentication and committee owner verification
     * Performance: Single database delete operation
     * Error Mapping: Service throws ResponseStatusException with appropriate status
     * Developer Notes: This is a hard delete; consider soft delete for audit trails
     * 
     * @param id the ID of the mapping to delete
     * @return ResponseEntity with 204 No Content status
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        log.info("Deleting comitte member map with ID: {}", id);
        service.delete(id);
        log.info("Deleted comitte member map with ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}
