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
 * REST controller for managing Member resources.
 * 
 * <p>This controller provides CRUD operations and search functionality for members.
 * Members can be associated with committees and participate in bidding activities.</p>
 * 
 * <p><b>Security Note:</b> All endpoints should be secured with HTTPS in production
 * and implement proper authorization checks via @PreAuthorize to ensure users can only
 * access/modify their own member records or records they have permission to manage.</p>
 * 
 * <p><b>Best Practices:</b>
 * <ul>
 *   <li>Validate all input data using @Valid annotations</li>
 *   <li>Implement pagination for search endpoints to improve performance</li>
 *   <li>Avoid logging or returning PII (Personally Identifiable Information)</li>
 *   <li>Consider soft-deletes and audit logs for delete operations</li>
 *   <li>Encrypt sensitive member data at rest and in transit</li>
 * </ul>
 * </p>
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
     * <p><b>Endpoint:</b> POST /api/members</p>
     * <p><b>Request Body:</b> MemberRequest (JSON) - Validated with @Valid</p>
     * <p><b>Response:</b> MemberResponse (JSON) with HTTP 201 Created</p>
     * 
     * <p><b>Developer Notes:</b>
     * <ul>
     *   <li>Input validation is performed via @Valid annotation</li>
     *   <li>TODO: Add @PreAuthorize to restrict member creation to authorized users</li>
     *   <li>Avoid logging sensitive PII like phone numbers, emails, addresses</li>
     *   <li>Ensure username/email uniqueness is enforced</li>
     *   <li>Consider rate-limiting to prevent abuse</li>
     *   <li>Errors are mapped to structured JSON responses via ApiExceptionHandler</li>
     * </ul>
     * </p>
     * 
     * @param dto the member creation request containing member details
     * @return ResponseEntity with MemberResponse and HTTP 201 status
     */
    @PostMapping
    public ResponseEntity<MemberResponse> create(@Valid @RequestBody MemberRequest dto) {
        log.info("Creating member");
        MemberResponse response = memberService.create(dto);
        log.info("Member created with ID: {}", response.memberId());
        return ResponseEntity.status(201).body(response);
    }

    /**
     * Retrieves a member by their ID.
     * 
     * <p><b>Endpoint:</b> GET /api/members/{memberId}</p>
     * <p><b>Path Variable:</b> memberId (Long) - The unique identifier of the member</p>
     * <p><b>Response:</b> MemberResponse (JSON) with HTTP 200 OK</p>
     * 
     * <p><b>Developer Notes:</b>
     * <ul>
     *   <li>TODO: Add @PreAuthorize to ensure users can only view their own profile or authorized members</li>
     *   <li>Returns HTTP 404 if member not found (handled by service layer)</li>
     *   <li>Avoid exposing sensitive PII in responses - use DTOs to control data exposure</li>
     *   <li>Consider caching for frequently accessed member profiles</li>
     *   <li>Errors are mapped to structured JSON responses via ApiExceptionHandler</li>
     * </ul>
     * </p>
     * 
     * @param memberId the ID of the member to retrieve
     * @return ResponseEntity with MemberResponse and HTTP 200 status
     */
    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponse> get(@PathVariable Long memberId) {
        log.info("Fetching member with ID: {}", memberId);
        MemberResponse response = memberService.get(memberId);
        return ResponseEntity.ok(response);
    }

    /**
     * Updates an existing member.
     * 
     * <p><b>Endpoint:</b> PUT /api/members/{memberId}</p>
     * <p><b>Path Variable:</b> memberId (Long) - The unique identifier of the member</p>
     * <p><b>Request Body:</b> MemberRequest (JSON) - Validated with @Valid</p>
     * <p><b>Response:</b> MemberResponse (JSON) with HTTP 200 OK</p>
     * 
     * <p><b>Developer Notes:</b>
     * <ul>
     *   <li>Input validation is performed via @Valid annotation</li>
     *   <li>TODO: Add @PreAuthorize to ensure only the member or admins can update</li>
     *   <li>Returns HTTP 404 if member not found</li>
     *   <li>Avoid logging sensitive PII in update operations</li>
     *   <li>Consider implementing optimistic locking to prevent concurrent update issues</li>
     *   <li>Maintain audit trail of updates for compliance</li>
     *   <li>Errors are mapped to structured JSON responses via ApiExceptionHandler</li>
     * </ul>
     * </p>
     * 
     * @param memberId the ID of the member to update
     * @param dto the updated member data
     * @return ResponseEntity with updated MemberResponse and HTTP 200 status
     */
    @PutMapping("/{memberId}")
    public ResponseEntity<MemberResponse> update(@PathVariable Long memberId, @Valid @RequestBody MemberRequest dto) {
        log.info("Updating member with ID: {}", memberId);
        MemberResponse response = memberService.update(memberId, dto);
        return ResponseEntity.ok(response);
    }

    /**
     * Deletes a member by their ID.
     * 
     * <p><b>Endpoint:</b> DELETE /api/members/{memberId}</p>
     * <p><b>Path Variable:</b> memberId (Long) - The unique identifier of the member</p>
     * <p><b>Response:</b> HTTP 204 No Content on success</p>
     * 
     * <p><b>Developer Notes:</b>
     * <ul>
     *   <li>TODO: Add @PreAuthorize to restrict deletion to the member or admins only</li>
     *   <li>Returns HTTP 404 if member not found</li>
     *   <li>TODO: Implement soft-delete instead of hard-delete for audit purposes</li>
     *   <li>Consider maintaining audit log of all delete operations</li>
     *   <li>Verify no dependent records exist before deletion (committee memberships, bids)</li>
     *   <li>May require cascading deletes or relationship cleanup</li>
     *   <li>Errors are mapped to structured JSON responses via ApiExceptionHandler</li>
     * </ul>
     * </p>
     * 
     * @param memberId the ID of the member to delete
     * @return ResponseEntity with HTTP 204 No Content status
     */
    @DeleteMapping("/{memberId}")
    public ResponseEntity<?> delete(@PathVariable Long memberId) {
        log.info("Deleting member with ID: {}", memberId);
        memberService.delete(memberId);
        log.info("Deleted member with ID: {}", memberId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Searches for members based on optional criteria.
     * 
     * <p><b>Endpoint:</b> GET /api/members/search</p>
     * <p><b>Query Parameters:</b>
     * <ul>
     *   <li>name (String, optional) - Search by member name</li>
     *   <li>mobile (String, optional) - Search by mobile number</li>
     *   <li>username (String, optional) - Search by username</li>
     * </ul>
     * </p>
     * <p><b>Response:</b> List of MemberResponse (JSON) with HTTP 200 OK</p>
     * 
     * <p><b>Developer Notes:</b>
     * <ul>
     *   <li>TODO: Add @PreAuthorize to restrict search to authorized users</li>
     *   <li>TODO: Implement pagination using additional @RequestParam (page, size, sort)</li>
     *   <li>TODO: Add logging for audit purposes</li>
     *   <li>Returns empty list if no members match the search criteria</li>
     *   <li>May return large datasets - pagination strongly recommended</li>
     *   <li>Consider implementing fuzzy search or partial matching</li>
     *   <li>Avoid exposing sensitive PII in search results</li>
     *   <li>Consider rate-limiting to prevent data mining</li>
     *   <li>Errors are mapped to structured JSON responses via ApiExceptionHandler</li>
     * </ul>
     * </p>
     * 
     * @param name optional name search parameter
     * @param mobile optional mobile search parameter
     * @param username optional username search parameter
     * @return ResponseEntity with List of MemberResponse and HTTP 200 status
     */
    @GetMapping("/search")
    public ResponseEntity<List<MemberResponse>> searchMembers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String mobile) {
        log.info("Searching members");
        List<MemberResponse> members = memberService.searchMembers(name, mobile);
        return ResponseEntity.ok(members);
    }
}
