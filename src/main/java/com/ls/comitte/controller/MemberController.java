package com.ls.comitte.controller;

import com.ls.comitte.model.request.MemberRequest;
import com.ls.comitte.model.response.MemberResponse;
import com.ls.comitte.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

/**
 * REST controller for managing members.
 * 
 * Provides endpoints for creating, retrieving, updating, deleting, and searching members.
 * Members are the core users of the committee system who can participate in committees
 * and place bids.
 * All endpoints require appropriate authentication and authorization.
 * 
 * Base Path: /api/members
 */
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;

    /**
     * Creates a new member.
     * 
     * Purpose: Registers a new member in the system
     * Request: MemberRequest (JSON body) - validated
     * Response: MemberResponse with created member details
     * Status Codes:
     *   - 201: Member created successfully
     *   - 400: Invalid input data (validation failure, duplicate username/mobile)
     *   - 500: Server error
     * 
     * Validation: Enforced via @Valid annotation on MemberRequest
     * Security: May require admin role for member creation
     * Performance: Single database insert with unique constraint checks
     * Error Mapping: Service validates unique constraints on username and mobile
     * Developer Notes: Sensitive data like passwords should be hashed before storage
     * 
     * @param dto the member creation request
     * @return ResponseEntity with created member details and 201 status
     */
    @PostMapping
    public ResponseEntity<MemberResponse> create(@Valid @RequestBody MemberRequest dto) {
        log.info("Creating member with data: {}", dto);
        MemberResponse response = memberService.create(dto);
        log.info("Member created with ID: {}", response.memberId());
        return ResponseEntity.status(201).body(response);
    }

    /**
     * Retrieves a member by ID.
     * 
     * Purpose: Fetches detailed information for a specific member
     * Request: Path variable memberId
     * Response: MemberResponse with member details (excluding sensitive data)
     * Status Codes:
     *   - 200: Member found and returned
     *   - 404: Member not found
     *   - 500: Server error
     * 
     * Security: Requires authentication; may restrict to member's own data or admin
     * Performance: Single database query by primary key
     * Error Mapping: Service throws ResponseStatusException with 404 if member not found
     * Developer Notes: Response excludes password and other sensitive fields
     * 
     * @param memberId the ID of the member to retrieve
     * @return ResponseEntity with member details and 200 status
     */
    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponse> get(@PathVariable Long memberId) {
        log.info("Fetching member with ID: {}", memberId);
        MemberResponse response = memberService.get(memberId);
        log.info("Fetched member: {}", response);
        return ResponseEntity.ok(response);
    }

    /**
     * Updates an existing member.
     * 
     * Purpose: Updates member information for a specific member ID
     * Request: Path variable memberId and MemberRequest (JSON body) - validated
     * Response: MemberResponse with updated member details
     * Status Codes:
     *   - 200: Member updated successfully
     *   - 400: Invalid input data (validation failure, duplicate username/mobile)
     *   - 404: Member not found
     *   - 403: Forbidden (user cannot update other members' data)
     *   - 500: Server error
     * 
     * Validation: Enforced via @Valid annotation on MemberRequest
     * Security: Requires authentication; users can only update their own data unless admin
     * Performance: Single database update operation
     * Error Mapping: Service validates unique constraints and ownership
     * Developer Notes: Password updates may require separate endpoint with current password verification
     * 
     * @param memberId the ID of the member to update
     * @param dto the updated member data
     * @return ResponseEntity with updated member details and 200 status
     */
    @PutMapping("/{memberId}")
    public ResponseEntity<MemberResponse> update(@PathVariable Long memberId, @Valid @RequestBody MemberRequest dto) {
        log.info("Updating member with ID: {} using data: {}", memberId, dto);
        MemberResponse response = memberService.update(memberId, dto);
        log.info("Updated member: {}", response);
        return ResponseEntity.ok(response);
    }

    /**
     * Deletes a member.
     * 
     * Purpose: Permanently removes a member from the system
     * Request: Path variable memberId
     * Response: No content
     * Status Codes:
     *   - 204: Member deleted successfully
     *   - 404: Member not found
     *   - 403: Forbidden (requires admin role)
     *   - 500: Server error
     * 
     * Security: Requires authentication and admin role
     * Performance: Database delete with cascading to related entities
     * Error Mapping: Service validates admin role and member existence
     * Developer Notes: This is a hard delete; consider soft delete for audit trails
     * 
     * @param memberId the ID of the member to delete
     * @return ResponseEntity with 204 No Content status
     */
    @DeleteMapping("/{memberId}")
    public ResponseEntity<?> delete(@PathVariable Long memberId) {
        log.info("Deleting member with ID: {}", memberId);
        memberService.delete(memberId);
        log.info("Deleted member with ID: {}", memberId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Searches for members by various criteria.
     * 
     * Purpose: Finds members matching the provided search criteria
     * Request: Query parameters - name, mobile, username (all optional)
     * Response: List of MemberResponse objects matching the criteria
     * Status Codes:
     *   - 200: Search completed successfully (empty list if no matches)
     *   - 400: Invalid search parameters
     *   - 500: Server error
     * 
     * Security: Requires authentication; may restrict results based on user role
     * Performance: Database query with indexed fields for efficient searching
     * Developer Notes: At least one parameter should be provided; consider pagination for large result sets
     * Error Mapping: Service validates search parameters
     * 
     * @param name optional name filter (partial match)
     * @param mobile optional mobile number filter (exact match)
     * @param username optional username filter (partial match)
     * @return ResponseEntity with list of matching members and 200 status
     */
    @GetMapping("/search")
    public ResponseEntity<List<MemberResponse>> searchMembers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String mobile,
            @RequestParam(required = false) String username) {

        List<MemberResponse> members = memberService.searchMembers(name, mobile, username);
        return ResponseEntity.ok(members);
    }
}
