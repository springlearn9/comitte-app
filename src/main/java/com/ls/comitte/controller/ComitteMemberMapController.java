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
 * REST controller for managing Committee-Member mapping resources.
 * 
 * <p>This controller provides CRUD operations for the relationship between
 * committees and members. It manages the association data including roles,
 * permissions, and other mapping-specific attributes.</p>
 * 
 * <p><b>Security Note:</b> All endpoints should be secured with HTTPS in production
 * and implement proper authorization checks via @PreAuthorize to ensure users can only
 * manage mappings they have permission for.</p>
 * 
 * <p><b>Best Practices:</b>
 * <ul>
 *   <li>Validate all input data using @Valid annotations</li>
 *   <li>Ensure referential integrity when creating/deleting mappings</li>
 *   <li>Avoid logging or returning PII (Personally Identifiable Information)</li>
 *   <li>Consider audit logs for all mapping changes</li>
 *   <li>Prevent duplicate mappings at the service layer</li>
 * </ul>
 * </p>
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
     * <p><b>Endpoint:</b> POST /api/comitte-member-map</p>
     * <p><b>Request Body:</b> ComitteMemberMapRequest (JSON) - Validated with @Valid</p>
     * <p><b>Response:</b> ComitteMemberMapResponse (JSON) with HTTP 201 Created</p>
     * 
     * <p><b>Developer Notes:</b>
     * <ul>
     *   <li>Input validation is performed via @Valid annotation</li>
     *   <li>TODO: Add @PreAuthorize to restrict mapping creation to authorized users</li>
     *   <li>Verify both committee and member exist before creating mapping</li>
     *   <li>Prevent duplicate mappings at service layer</li>
     *   <li>Consider setting default role/permissions for new mappings</li>
     *   <li>Errors are mapped to structured JSON responses via ApiExceptionHandler</li>
     * </ul>
     * </p>
     * 
     * @param dto the mapping request containing committee and member details
     * @return ResponseEntity with ComitteMemberMapResponse and HTTP 201 status
     */
    @PostMapping
    public ResponseEntity<ComitteMemberMapResponse> create(@Valid @RequestBody ComitteMemberMapRequest dto) {
        log.info("Creating comitte member map");
        ComitteMemberMapResponse response = service.create(dto);
        log.info("Comitte member map created with ID: {}", response.id());
        return ResponseEntity.status(201).body(response);
    }

    /**
     * Retrieves a committee-member mapping by its ID.
     * 
     * <p><b>Endpoint:</b> GET /api/comitte-member-map/{id}</p>
     * <p><b>Path Variable:</b> id (Long) - The unique identifier of the mapping</p>
     * <p><b>Response:</b> ComitteMemberMapResponse (JSON) with HTTP 200 OK</p>
     * 
     * <p><b>Developer Notes:</b>
     * <ul>
     *   <li>TODO: Add @PreAuthorize to ensure users can only view authorized mappings</li>
     *   <li>Returns HTTP 404 if mapping not found (handled by service layer)</li>
     *   <li>Response includes mapping details but should not expose sensitive PII</li>
     *   <li>Errors are mapped to structured JSON responses via ApiExceptionHandler</li>
     * </ul>
     * </p>
     * 
     * @param id the ID of the mapping to retrieve
     * @return ResponseEntity with ComitteMemberMapResponse and HTTP 200 status
     */
    @GetMapping("/{id}")
    public ResponseEntity<ComitteMemberMapResponse> get(@PathVariable Long id) {
        log.info("Fetching comitte member map for ID: {}", id);
        ComitteMemberMapResponse response = service.get(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Updates an existing committee-member mapping.
     * 
     * <p><b>Endpoint:</b> PUT /api/comitte-member-map/{id}</p>
     * <p><b>Path Variable:</b> id (Long) - The unique identifier of the mapping</p>
     * <p><b>Request Body:</b> ComitteMemberMapRequest (JSON) - Validated with @Valid</p>
     * <p><b>Response:</b> ComitteMemberMapResponse (JSON) with HTTP 200 OK</p>
     * 
     * <p><b>Developer Notes:</b>
     * <ul>
     *   <li>Input validation is performed via @Valid annotation</li>
     *   <li>TODO: Add @PreAuthorize to ensure only authorized users can update mappings</li>
     *   <li>Returns HTTP 404 if mapping not found</li>
     *   <li>Consider implementing optimistic locking to prevent concurrent updates</li>
     *   <li>Maintain audit trail of updates for compliance</li>
     *   <li>Verify that committee and member IDs remain valid</li>
     *   <li>Errors are mapped to structured JSON responses via ApiExceptionHandler</li>
     * </ul>
     * </p>
     * 
     * @param id the ID of the mapping to update
     * @param request the updated mapping data
     * @return ResponseEntity with updated ComitteMemberMapResponse and HTTP 200 status
     */
    @PutMapping("/{id}")
    public ResponseEntity<ComitteMemberMapResponse> update(@PathVariable Long id, @Valid @RequestBody ComitteMemberMapRequest request) {
        log.info("Updating comitte member map with ID: {}", id);
        ComitteMemberMapResponse response = service.update(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Deletes a committee-member mapping by its ID.
     * 
     * <p><b>Endpoint:</b> DELETE /api/comitte-member-map/{id}</p>
     * <p><b>Path Variable:</b> id (Long) - The unique identifier of the mapping</p>
     * <p><b>Response:</b> HTTP 204 No Content on success</p>
     * 
     * <p><b>Developer Notes:</b>
     * <ul>
     *   <li>TODO: Add @PreAuthorize to restrict deletion to authorized users only</li>
     *   <li>Returns HTTP 404 if mapping not found</li>
     *   <li>TODO: Implement soft-delete instead of hard-delete for audit purposes</li>
     *   <li>Consider maintaining audit log of all mapping deletions</li>
     *   <li>Verify impact on committee/member before deletion</li>
     *   <li>Errors are mapped to structured JSON responses via ApiExceptionHandler</li>
     * </ul>
     * </p>
     * 
     * @param id the ID of the mapping to delete
     * @return ResponseEntity with HTTP 204 No Content status
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        log.info("Deleting comitte member map with ID: {}", id);
        service.delete(id);
        log.info("Deleted comitte member map with ID: {}", id);
        return ResponseEntity.noContent().build();
    }

    
}
