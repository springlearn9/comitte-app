package com.ls.comitte.controller;

import com.ls.comitte.model.response.BidResponse;
import com.ls.comitte.model.request.ComitteRequest;
import com.ls.comitte.model.response.ComitteResponse;
import com.ls.comitte.model.response.MemberResponse;
import com.ls.comitte.service.BidService;
import com.ls.comitte.service.ComitteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

/**
 * REST controller for managing committees.
 * 
 * Provides endpoints for creating, retrieving, updating, and deleting committees,
 * as well as managing committee members and viewing committee bids.
 * All endpoints require appropriate authentication and authorization.
 * 
 * Base Path: /api/comittes
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
     * Purpose: Creates a new committee in the system
     * Request: ComitteRequest (JSON body) - validated
     * Response: ComitteResponse with created committee details
     * Status Codes:
     *   - 201: Committee created successfully
     *   - 400: Invalid input data (validation failure)
     *   - 500: Server error
     * 
     * Validation: Enforced via @Valid annotation on ComitteRequest
     * Security: Requires authentication; user becomes committee owner
     * Performance: Single database insert with owner relationship
     * 
     * @param dto the committee creation request
     * @return ResponseEntity with created committee details and 201 status
     */
    @PostMapping
    public ResponseEntity<ComitteResponse> create(@Valid @RequestBody ComitteRequest dto) {
        log.info("Creating comitte with data: {}", dto);
        ComitteResponse response = comitteService.create(dto);
        log.info("Comitte created with ID: {}", response.comitteId());
        return ResponseEntity.status(201).body(response);
    }

    /**
     * Retrieves a committee by ID.
     * 
     * Purpose: Fetches detailed information for a specific committee
     * Request: Path variable comitteId
     * Response: ComitteResponse with committee details
     * Status Codes:
     *   - 200: Committee found and returned
     *   - 404: Committee not found
     *   - 500: Server error
     * 
     * Security: Requires authentication; may restrict based on membership
     * Performance: Single database query by primary key
     * Error Mapping: Service throws ResponseStatusException with 404 if committee not found
     * 
     * @param comitteId the ID of the committee to retrieve
     * @return ResponseEntity with committee details and 200 status
     */
    @GetMapping("/{comitteId}")
    public ResponseEntity<ComitteResponse> get(@PathVariable Long comitteId) {
        log.info("Fetching comitte with ID: {}", comitteId);
        ComitteResponse response = comitteService.get(comitteId);
        log.info("Fetched comitte: {}", response);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves all committees for a member.
     * 
     * Purpose: Fetches all committees that a specific member belongs to
     * Request: Path variable memberId
     * Response: List of ComitteResponse objects
     * Status Codes:
     *   - 200: Committees retrieved successfully (empty list if none found)
     *   - 404: Member not found
     *   - 500: Server error
     * 
     * Security: Requires authentication; may restrict to member's own data
     * Performance: Database query with join on committee-member mapping
     * Developer Notes: Consider pagination for members in many committees
     * 
     * @param memberId the ID of the member
     * @return ResponseEntity with list of committees and 200 status
     */
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<ComitteResponse>> getMemberComittes(@PathVariable Long memberId) {
        log.info("Fetching comittes for member ID: {}", memberId);
        List<ComitteResponse> comittes = comitteService.getMemberComittes(memberId);
        log.info("Fetched comittes: {}", comittes);
        return ResponseEntity.ok(comittes);
    }

    /**
     * Retrieves all committees owned by a member.
     * 
     * Purpose: Fetches all committees where the member is the owner
     * Request: Path variable ownerId
     * Response: List of ComitteResponse objects
     * Status Codes:
     *   - 200: Committees retrieved successfully (empty list if none found)
     *   - 404: Owner not found
     *   - 500: Server error
     * 
     * Security: Requires authentication; may restrict to owner's own data
     * Performance: Database query filtering by owner ID
     * Developer Notes: Consider pagination for owners with many committees
     * 
     * @param ownerId the ID of the owner
     * @return ResponseEntity with list of owned committees and 200 status
     */
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<ComitteResponse>> getOwnerComittes(@PathVariable Long ownerId) {
        log.info("Fetching comittes for owner ID: {}", ownerId);
        List<ComitteResponse> comittes = comitteService.getOwnerComittes(ownerId);
        log.info("Fetched comittes: {}", comittes);
        return ResponseEntity.ok(comittes);
    }

    /**
     * Updates an existing committee.
     * 
     * Purpose: Updates committee information for a specific committee ID
     * Request: Path variable comitteId and ComitteRequest (JSON body) - validated
     * Response: ComitteResponse with updated committee details
     * Status Codes:
     *   - 200: Committee updated successfully
     *   - 400: Invalid input data (validation failure)
     *   - 404: Committee not found
     *   - 403: Forbidden (user is not the committee owner)
     *   - 500: Server error
     * 
     * Validation: Enforced via @Valid annotation on ComitteRequest
     * Security: Requires authentication and ownership verification
     * Performance: Single database update operation
     * Error Mapping: Service throws ResponseStatusException with appropriate status
     * 
     * @param comitteId the ID of the committee to update
     * @param dto the updated committee data
     * @return ResponseEntity with updated committee details and 200 status
     */
    @PutMapping("/{comitteId}")
    public ResponseEntity<ComitteResponse> update(@PathVariable Long comitteId, @Valid @RequestBody ComitteRequest dto) {
        log.info("Updating comitte with ID: {} using data: {}", comitteId, dto);
        ComitteResponse response = comitteService.update(comitteId, dto);
        log.info("Updated comitte: {}", response);
        return ResponseEntity.ok(response);
    }

    /**
     * Deletes a committee.
     * 
     * Purpose: Permanently removes a committee from the system
     * Request: Path variable comitteId
     * Response: No content
     * Status Codes:
     *   - 204: Committee deleted successfully
     *   - 404: Committee not found
     *   - 403: Forbidden (user is not the committee owner)
     *   - 500: Server error
     * 
     * Security: Requires authentication and ownership verification
     * Performance: Database delete with cascading to related entities
     * Error Mapping: Service throws ResponseStatusException with appropriate status
     * Developer Notes: This is a hard delete; consider soft delete for audit trails
     * 
     * @param comitteId the ID of the committee to delete
     * @return ResponseEntity with 204 No Content status
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
     * Purpose: Associates multiple members with a committee
     * Request: Path variable comitteId and List of member IDs (JSON body)
     * Response: ComitteResponse with updated member list
     * Status Codes:
     *   - 200: Members assigned successfully
     *   - 400: Invalid member IDs
     *   - 404: Committee or member(s) not found
     *   - 403: Forbidden (user is not the committee owner)
     *   - 500: Server error
     * 
     * Security: Requires authentication and ownership verification
     * Performance: Batch insert for committee-member mappings
     * Error Mapping: Service validates member existence before assignment
     * Developer Notes: Existing members are not removed; use replace endpoint for that
     * 
     * @param comitteId the ID of the committee
     * @param memberIds the list of member IDs to assign
     * @return ResponseEntity with updated committee details and 200 status
     */
    @PostMapping("/{comitteId}/assign-members")
    public ResponseEntity<ComitteResponse> assign(@PathVariable Long comitteId, @RequestBody List<Long> memberIds) {
        log.info("Assigning members to comitte ID: {} with member IDs: {}", comitteId, memberIds);
        ComitteResponse response = comitteService.assignMembers(comitteId, memberIds);
        log.info("Assigned members to comitte: {}", response);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves all bids for a committee.
     * 
     * Purpose: Fetches all bids associated with a specific committee
     * Request: Path variable comitteId
     * Response: List of BidResponse objects
     * Status Codes:
     *   - 200: Bids retrieved successfully (empty list if none found)
     *   - 404: Committee not found
     *   - 500: Server error
     * 
     * Security: Requires authentication; may restrict based on committee membership
     * Performance: Database query with filtering by committee ID
     * Developer Notes: Consider pagination for committees with many bids
     * 
     * @param comitteId the ID of the committee
     * @return ResponseEntity with list of bids and 200 status
     */
    @GetMapping("/{comitteId}/bids")
    public ResponseEntity<List<BidResponse>> getBidsByComitteId(@PathVariable Long comitteId) {
        log.info("Fetching bids for comitte ID: {}", comitteId);
        List<BidResponse> bids = bidService.getBidsByComitteId(comitteId);
        log.info("Fetched bids: {}", bids);
        return ResponseEntity.ok(bids);
    }

    /**
     * Retrieves all members associated with a committee.
     * 
     * Purpose: Fetches all members belonging to a specific committee
     * Request: Path variable comitteId
     * Response: List of MemberResponse objects
     * Status Codes:
     *   - 200: Members retrieved successfully (empty list if none found)
     *   - 404: Committee not found
     *   - 500: Server error
     * 
     * Security: Requires authentication; may restrict based on committee membership
     * Performance: Database query with join on committee-member mapping
     * Developer Notes: Consider pagination for large committees
     * 
     * @param comitteId the ID of the committee
     * @return ResponseEntity with list of members and 200 status
     */
    @GetMapping("/{comitteId}/members")
    public ResponseEntity<List<MemberResponse>> getAllAssociatedMembers(@PathVariable Long comitteId) {
        List<MemberResponse> members = comitteService.getAllAssociatedMembers(comitteId);
        return ResponseEntity.ok(members);
    }
}
