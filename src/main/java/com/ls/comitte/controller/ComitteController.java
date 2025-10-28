package com.ls.comitte.controller;

import com.ls.comitte.model.response.BidResponse;
import com.ls.comitte.model.response.ComitteMemberMapResponse;
import com.ls.comitte.model.request.ComitteRequest;
import com.ls.comitte.model.response.ComitteResponse;
import com.ls.comitte.service.BidService;
import com.ls.comitte.service.ComitteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

/**
 * REST controller for managing Committee (Comitte) resources.
 * 
 * <p>This controller provides CRUD operations and committee-specific functionality
 * including member management and bid retrieval. Committees are organizational units
 * that can have members and associated bids.</p>
 * 
 * <p><b>Security Note:</b> All endpoints should be secured with HTTPS in production
 * and implement proper authorization checks via @PreAuthorize to ensure users can only
 * access/modify committees they have permission for.</p>
 * 
 * <p><b>Best Practices:</b>
 * <ul>
 *   <li>Validate all input data using @Valid annotations</li>
 *   <li>Implement pagination for list endpoints returning multiple results</li>
 *   <li>Avoid logging or returning PII (Personally Identifiable Information)</li>
 *   <li>Consider soft-deletes and audit logs for delete operations</li>
 *   <li>Cache frequently accessed committee data for performance</li>
 * </ul>
 * </p>
 */
@RestController
@RequestMapping("/api/comittes")
@RequiredArgsConstructor
@Slf4j
public class ComitteController {
    private final ComitteService comitteService;
    private final BidService bidService;

    /**
     * Creates a new committee.
     * 
     * <p><b>Endpoint:</b> POST /api/comittes</p>
     * <p><b>Request Body:</b> ComitteRequest (JSON) - Validated with @Valid</p>
     * <p><b>Response:</b> ComitteResponse (JSON) with HTTP 201 Created</p>
     * 
     * <p><b>Developer Notes:</b>
     * <ul>
     *   <li>Input validation is performed via @Valid annotation</li>
     *   <li>TODO: Add @PreAuthorize to restrict committee creation to authorized users</li>
     *   <li>Creator typically becomes the committee owner</li>
     *   <li>Consider setting default values for committee properties</li>
     *   <li>Errors are mapped to structured JSON responses via ApiExceptionHandler</li>
     * </ul>
     * </p>
     * 
     * @param dto the committee creation request containing committee details
     * @return ResponseEntity with ComitteResponse and HTTP 201 status
     */
    @PostMapping
    public ResponseEntity<ComitteResponse> create(@Valid @RequestBody ComitteRequest dto) {
        log.info("Creating comitte");
        ComitteResponse response = comitteService.create(dto);
        log.info("Comitte created with ID: {}", response.comitteId());
        return ResponseEntity.status(201).body(response);
    }

    /**
     * Retrieves a committee by its ID.
     * 
     * <p><b>Endpoint:</b> GET /api/comittes/{comitteId}</p>
     * <p><b>Path Variable:</b> comitteId (Long) - The unique identifier of the committee</p>
     * <p><b>Response:</b> ComitteResponse (JSON) with HTTP 200 OK</p>
     * 
     * <p><b>Developer Notes:</b>
     * <ul>
     *   <li>TODO: Add @PreAuthorize to ensure users can only view authorized committees</li>
     *   <li>Returns HTTP 404 if committee not found (handled by service layer)</li>
     *   <li>Response includes committee details but should not expose sensitive PII</li>
     *   <li>Consider caching for frequently accessed committees</li>
     *   <li>Errors are mapped to structured JSON responses via ApiExceptionHandler</li>
     * </ul>
     * </p>
     * 
     * @param comitteId the ID of the committee to retrieve
     * @return ResponseEntity with ComitteResponse and HTTP 200 status
     */
    @GetMapping("/{comitteId}")
    public ResponseEntity<ComitteResponse> get(@PathVariable Long comitteId) {
        log.info("Fetching comitte with ID: {}", comitteId);
        ComitteResponse response = comitteService.get(comitteId);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves all committees associated with a specific member.
     * 
     * <p><b>Endpoint:</b> GET /api/comittes/member/{memberId}</p>
     * <p><b>Path Variable:</b> memberId (Long) - The unique identifier of the member</p>
     * <p><b>Response:</b> List of ComitteResponse (JSON) with HTTP 200 OK</p>
     * 
     * <p><b>Developer Notes:</b>
     * <ul>
     *   <li>TODO: Add @PreAuthorize to ensure users can only view their own committees</li>
     *   <li>TODO: Implement pagination using @RequestParam (page, size, sort)</li>
     *   <li>Returns empty list if member has no committee associations</li>
     *   <li>May return large datasets - pagination strongly recommended</li>
     *   <li>Consider caching member-committee associations</li>
     *   <li>Errors are mapped to structured JSON responses via ApiExceptionHandler</li>
     * </ul>
     * </p>
     * 
     * @param memberId the ID of the member to retrieve committees for
     * @return ResponseEntity with List of ComitteResponse and HTTP 200 status
     */
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<ComitteResponse>> getMemberComittes(@PathVariable Long memberId) {
        log.info("Fetching comittes for member ID: {}", memberId);
        List<ComitteResponse> comittes = comitteService.getMemberComittes(memberId);
        return ResponseEntity.ok(comittes);
    }

    /**
     * Retrieves all committees owned by a specific owner.
     * 
     * <p><b>Endpoint:</b> GET /api/comittes/owner/{ownerId}</p>
     * <p><b>Path Variable:</b> ownerId (Long) - The unique identifier of the owner</p>
     * <p><b>Response:</b> List of ComitteResponse (JSON) with HTTP 200 OK</p>
     * 
     * <p><b>Developer Notes:</b>
     * <ul>
     *   <li>TODO: Add @PreAuthorize to ensure users can only view their owned committees</li>
     *   <li>TODO: Implement pagination using @RequestParam (page, size, sort)</li>
     *   <li>Returns empty list if owner has no committees</li>
     *   <li>May return large datasets - pagination strongly recommended</li>
     *   <li>Consider caching owner-committee associations</li>
     *   <li>Errors are mapped to structured JSON responses via ApiExceptionHandler</li>
     * </ul>
     * </p>
     * 
     * @param ownerId the ID of the owner to retrieve committees for
     * @return ResponseEntity with List of ComitteResponse and HTTP 200 status
     */
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<ComitteResponse>> getOwnerComittes(@PathVariable Long ownerId) {
        log.info("Fetching comittes for owner ID: {}", ownerId);
        List<ComitteResponse> comittes = comitteService.getOwnerComittes(ownerId);
        return ResponseEntity.ok(comittes);
    }

    /**
     * Updates an existing committee.
     * 
     * <p><b>Endpoint:</b> PUT /api/comittes/{comitteId}</p>
     * <p><b>Path Variable:</b> comitteId (Long) - The unique identifier of the committee</p>
     * <p><b>Request Body:</b> ComitteRequest (JSON) - Validated with @Valid</p>
     * <p><b>Response:</b> ComitteResponse (JSON) with HTTP 200 OK</p>
     * 
     * <p><b>Developer Notes:</b>
     * <ul>
     *   <li>Input validation is performed via @Valid annotation</li>
     *   <li>TODO: Add @PreAuthorize to ensure only committee owners/admins can update</li>
     *   <li>Returns HTTP 404 if committee not found</li>
     *   <li>Consider implementing optimistic locking to prevent concurrent updates</li>
     *   <li>Maintain audit trail of updates for compliance</li>
     *   <li>Errors are mapped to structured JSON responses via ApiExceptionHandler</li>
     * </ul>
     * </p>
     * 
     * @param comitteId the ID of the committee to update
     * @param dto the updated committee data
     * @return ResponseEntity with updated ComitteResponse and HTTP 200 status
     */
    @PutMapping("/{comitteId}")
    public ResponseEntity<ComitteResponse> update(@PathVariable Long comitteId, @Valid @RequestBody ComitteRequest dto) {
        log.info("Updating comitte with ID: {}", comitteId);
        ComitteResponse response = comitteService.update(comitteId, dto);
        return ResponseEntity.ok(response);
    }

    /**
     * Deletes a committee by its ID.
     * 
     * <p><b>Endpoint:</b> DELETE /api/comittes/{comitteId}</p>
     * <p><b>Path Variable:</b> comitteId (Long) - The unique identifier of the committee</p>
     * <p><b>Response:</b> HTTP 204 No Content on success</p>
     * 
     * <p><b>Developer Notes:</b>
     * <ul>
     *   <li>TODO: Add @PreAuthorize to restrict deletion to committee owners/admins only</li>
     *   <li>Returns HTTP 404 if committee not found</li>
     *   <li>TODO: Implement soft-delete instead of hard-delete for audit purposes</li>
     *   <li>Consider maintaining audit log of all delete operations</li>
     *   <li>Verify no dependent records (members, bids) exist before deletion</li>
     *   <li>May require cascading deletes or relationship cleanup</li>
     *   <li>Errors are mapped to structured JSON responses via ApiExceptionHandler</li>
     * </ul>
     * </p>
     * 
     * @param comitteId the ID of the committee to delete
     * @return ResponseEntity with HTTP 204 No Content status
     */
    @DeleteMapping("/{comitteId}")
    public ResponseEntity<?> delete(@PathVariable Long comitteId) {
        log.info("Deleting comitte with ID: {}", comitteId);
        comitteService.delete(comitteId);
        log.info("Deleted comitte with ID: {}", comitteId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Assigns members to a committee.
     * 
     * <p><b>Endpoint:</b> POST /api/comittes/{comitteId}/assign-members</p>
     * <p><b>Path Variable:</b> comitteId (Long) - The unique identifier of the committee</p>
     * <p><b>Request Body:</b> List of Long (JSON) - Array of member IDs to assign</p>
     * <p><b>Response:</b> ComitteResponse (JSON) with HTTP 200 OK</p>
     * 
     * <p><b>Developer Notes:</b>
     * <ul>
     *   <li>TODO: Add @Valid annotation with custom validator for member ID list</li>
     *   <li>TODO: Add @PreAuthorize to restrict to committee owners/admins only</li>
     *   <li>Verify all member IDs exist before assignment</li>
     *   <li>Consider checking for duplicate assignments</li>
     *   <li>Handle partial failures gracefully (e.g., some IDs invalid)</li>
     *   <li>Consider implementing bulk operations with transaction support</li>
     *   <li>Errors are mapped to structured JSON responses via ApiExceptionHandler</li>
     * </ul>
     * </p>
     * 
     * @param comitteId the ID of the committee to assign members to
     * @param memberIds list of member IDs to assign to the committee
     * @return ResponseEntity with updated ComitteResponse and HTTP 200 status
     */
    @PostMapping("/{comitteId}/assign-members")
    public ResponseEntity<ComitteResponse> assign(@PathVariable Long comitteId, @RequestBody List<Long> memberIds) {
        log.info("Assigning members to comitte ID: {}", comitteId);
        ComitteResponse response = comitteService.assignMembers(comitteId, memberIds);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves all bids associated with a specific committee.
     * 
     * <p><b>Endpoint:</b> GET /api/comittes/{comitteId}/bids</p>
     * <p><b>Path Variable:</b> comitteId (Long) - The unique identifier of the committee</p>
     * <p><b>Response:</b> List of BidResponse (JSON) with HTTP 200 OK</p>
     * 
     * <p><b>Developer Notes:</b>
     * <ul>
     *   <li>TODO: Add @PreAuthorize to ensure users can only view authorized committees</li>
     *   <li>TODO: Implement pagination using @RequestParam (page, size, sort)</li>
     *   <li>Returns empty list if no bids found for the committee</li>
     *   <li>May return large datasets - pagination strongly recommended</li>
     *   <li>Consider caching for frequently accessed committees</li>
     *   <li>Errors are mapped to structured JSON responses via ApiExceptionHandler</li>
     * </ul>
     * </p>
     * 
     * @param comitteId the ID of the committee to retrieve bids for
     * @return ResponseEntity with List of BidResponse and HTTP 200 status
     */
    @GetMapping("/{comitteId}/bids")
    public ResponseEntity<List<BidResponse>> getBidsByComitteId(@PathVariable Long comitteId) {
        log.info("Fetching bids for comitte ID: {}", comitteId);
        List<BidResponse> bids = bidService.getBidsByComitteId(comitteId);
        return ResponseEntity.ok(bids);
    }

    /**
     * Retrieves all members associated with a specific committee.
     * 
     * <p><b>Endpoint:</b> GET /api/comittes/{comitteId}/members</p>
     * <p><b>Path Variable:</b> comitteId (Long) - The unique identifier of the committee</p>
     * <p><b>Response:</b> List of MemberResponse (JSON) with HTTP 200 OK</p>
     * 
     * <p><b>Developer Notes:</b>
     * <ul>
     *   <li>TODO: Add @PreAuthorize to ensure users can only view authorized committees</li>
     *   <li>TODO: Implement pagination using @RequestParam (page, size, sort)</li>
     *   <li>TODO: Add logging for audit purposes</li>
     *   <li>Returns empty list if no members assigned to the committee</li>
     *   <li>May return large datasets - pagination strongly recommended</li>
     *   <li>Avoid exposing sensitive member PII in responses</li>
     *   <li>Errors are mapped to structured JSON responses via ApiExceptionHandler</li>
     * </ul>
     * </p>
     * 
     * @param comitteId the ID of the committee to retrieve members for
     * @return ResponseEntity with List of ComitteMemberMapResponse and HTTP 200 status
     */
    @GetMapping("/{comitteId}/members")
    public ResponseEntity<List<ComitteMemberMapResponse>> getAllAssociatedMembers(@PathVariable Long comitteId) {
        log.info("Fetching ComitteMemberMapResponse for comitte ID: {}", comitteId);
        List<ComitteMemberMapResponse> members = comitteService.getAllAssociatedMembers(comitteId);
        return ResponseEntity.ok(members);
    }
}
