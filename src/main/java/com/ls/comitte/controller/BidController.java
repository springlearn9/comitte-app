package com.ls.comitte.controller;

import com.ls.comitte.model.request.BidRequest;
import com.ls.comitte.model.response.BidResponse;
import com.ls.comitte.service.BidService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

/**
 * REST controller for managing Bid resources.
 * 
 * <p>This controller provides CRUD operations and bid-specific functionality
 * for managing bids within committees. All endpoints require proper authentication
 * and authorization (to be enforced via @PreAuthorize in future updates).</p>
 * 
 * <p><b>Security Note:</b> All endpoints should be secured with HTTPS in production
 * and implement proper authorization checks to ensure users can only access/modify
 * their own bids or bids they have permission to manage.</p>
 * 
 * <p><b>Best Practices:</b>
 * <ul>
 *   <li>Validate all input data using @Valid annotations</li>
 *   <li>Implement pagination for list endpoints to improve performance</li>
 *   <li>Avoid logging or returning PII (Personally Identifiable Information)</li>
 *   <li>Consider soft-deletes and audit logs for delete operations</li>
 * </ul>
 * </p>
 */
@RestController
@RequestMapping("/api/bids")
@RequiredArgsConstructor
@Slf4j
public class BidController {
    private final BidService bidService;

    /**
     * Creates a new bid.
     * 
     * <p><b>Endpoint:</b> POST /api/bids</p>
     * <p><b>Request Body:</b> BidRequest (JSON) - Validated with @Valid</p>
     * <p><b>Response:</b> BidResponse (JSON) with HTTP 201 Created</p>
     * 
     * <p><b>Developer Notes:</b>
     * <ul>
     *   <li>Input validation is performed via @Valid annotation</li>
     *   <li>TODO: Add @PreAuthorize to restrict access to authenticated users</li>
     *   <li>TODO: Verify user has permission to create bids in the target committee</li>
     *   <li>Consider rate-limiting to prevent abuse</li>
     *   <li>Errors are mapped to structured JSON responses via ApiExceptionHandler</li>
     * </ul>
     * </p>
     * 
     * @param bidRequest the bid creation request containing bid details
     * @return ResponseEntity with BidResponse and HTTP 201 status
     */
    @PostMapping
    public ResponseEntity<BidResponse> create(@Valid @RequestBody BidRequest bidRequest) {
        log.info("Creating bid");
        BidResponse response = bidService.create(bidRequest);
        log.info("Bid created with ID: {}", response.bidId());
        return ResponseEntity.status(201).body(response);
    }

    /**
     * Retrieves a bid by its ID.
     * 
     * <p><b>Endpoint:</b> GET /api/bids/{bidId}</p>
     * <p><b>Path Variable:</b> bidId (Long) - The unique identifier of the bid</p>
     * <p><b>Response:</b> BidResponse (JSON) with HTTP 200 OK</p>
     * 
     * <p><b>Developer Notes:</b>
     * <ul>
     *   <li>TODO: Add @PreAuthorize to ensure users can only view authorized bids</li>
     *   <li>Returns HTTP 404 if bid not found (handled by service layer)</li>
     *   <li>Response includes bid details but should not expose sensitive PII</li>
     *   <li>Consider caching for frequently accessed bids</li>
     *   <li>Errors are mapped to structured JSON responses via ApiExceptionHandler</li>
     * </ul>
     * </p>
     * 
     * @param bidId the ID of the bid to retrieve
     * @return ResponseEntity with BidResponse and HTTP 200 status
     */
    @GetMapping("/{bidId}")
    public ResponseEntity<BidResponse> get(@PathVariable Long bidId) {
        log.info("Fetching bid with ID: {}", bidId);
        BidResponse response = bidService.get(bidId);
        return ResponseEntity.ok(response);
    }

    /**
     * Updates an existing bid.
     * 
     * <p><b>Endpoint:</b> PUT /api/bids/{bidId}</p>
     * <p><b>Path Variable:</b> bidId (Long) - The unique identifier of the bid</p>
     * <p><b>Request Body:</b> BidRequest (JSON) - Validated with @Valid</p>
     * <p><b>Response:</b> BidResponse (JSON) with HTTP 200 OK</p>
     * 
     * <p><b>Developer Notes:</b>
     * <ul>
     *   <li>Input validation is performed via @Valid annotation</li>
     *   <li>TODO: Add @PreAuthorize to ensure only bid owners/admins can update</li>
     *   <li>Returns HTTP 404 if bid not found</li>
     *   <li>Consider implementing optimistic locking to prevent concurrent update issues</li>
     *   <li>Maintain audit trail of updates for compliance</li>
     *   <li>Errors are mapped to structured JSON responses via ApiExceptionHandler</li>
     * </ul>
     * </p>
     * 
     * @param bidId the ID of the bid to update
     * @param bidRequest the updated bid data
     * @return ResponseEntity with updated BidResponse and HTTP 200 status
     */
    @PutMapping("/{bidId}")
    public ResponseEntity<BidResponse> update(@PathVariable Long bidId, @Valid @RequestBody BidRequest bidRequest) {
        log.info("Updating bid with ID: {}", bidId);
        BidResponse response = bidService.update(bidId, bidRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * Deletes a bid by its ID.
     * 
     * <p><b>Endpoint:</b> DELETE /api/bids/{bidId}</p>
     * <p><b>Path Variable:</b> bidId (Long) - The unique identifier of the bid</p>
     * <p><b>Response:</b> HTTP 204 No Content on success</p>
     * 
     * <p><b>Developer Notes:</b>
     * <ul>
     *   <li>TODO: Add @PreAuthorize to restrict deletion to authorized users only</li>
     *   <li>Returns HTTP 404 if bid not found</li>
     *   <li>TODO: Implement soft-delete instead of hard-delete for audit purposes</li>
     *   <li>Consider maintaining audit log of all delete operations</li>
     *   <li>Verify no dependent records exist before deletion</li>
     *   <li>Errors are mapped to structured JSON responses via ApiExceptionHandler</li>
     * </ul>
     * </p>
     * 
     * @param bidId the ID of the bid to delete
     * @return ResponseEntity with HTTP 204 No Content status
     */
    @DeleteMapping("/{bidId}")
    public ResponseEntity<?> delete(@PathVariable Long bidId) {
        log.info("Deleting bid with ID: {}", bidId);
        bidService.delete(bidId);
        log.info("Deleted bid with ID: {}", bidId);
        return ResponseEntity.noContent().build();
    }



    /**
     * Retrieves the bid history for a specific bid.
     * 
     * <p><b>Endpoint:</b> GET /api/bids/{bidId}/history</p>
     * <p><b>Path Variable:</b> bidId (Long) - The unique identifier of the bid</p>
     * <p><b>Response:</b> BidResponse (JSON) with HTTP 200 OK</p>
     * 
     * <p><b>Developer Notes:</b>
     * <ul>
     *   <li>Currently returns the same data as GET /api/bids/{bidId}</li>
     *   <li>TODO: Add @PreAuthorize for access control</li>
     *   <li>TODO: Implement dedicated history tracking with timestamps</li>
     *   <li>Consider pagination for bids with extensive history</li>
     *   <li>Include all bid placements with timestamps and member info</li>
     *   <li>Errors are mapped to structured JSON responses via ApiExceptionHandler</li>
     * </ul>
     * </p>
     * 
     * @param bidId the ID of the bid to retrieve history for
     * @return ResponseEntity with BidResponse and HTTP 200 status
     */
    @GetMapping("/{bidId}/history")
    public ResponseEntity<BidResponse> history(@PathVariable Long bidId) {
        log.info("Fetching history for bid ID: {}", bidId);
        BidResponse response = bidService.get(bidId);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves all bids associated with a specific committee.
     * 
     * <p><b>Endpoint:</b> GET /api/bids/comitte/{comitteId}</p>
     * <p><b>Path Variable:</b> comitteId (Long) - The unique identifier of the committee</p>
     * <p><b>Response:</b> List of BidResponse (JSON) with HTTP 200 OK</p>
     * 
     * <p><b>Developer Notes:</b>
     * <ul>
     *   <li>TODO: Add @PreAuthorize to ensure users can only view authorized committees</li>
     *   <li>TODO: Implement pagination using @RequestParam (page, size, sort)</li>
     *   <li>Returns empty list if no bids found for the committee</li>
     *   <li>Consider caching for frequently accessed committees</li>
     *   <li>May return large datasets - pagination strongly recommended</li>
     *   <li>Errors are mapped to structured JSON responses via ApiExceptionHandler</li>
     * </ul>
     * </p>
     * 
     * @param comitteId the ID of the committee to retrieve bids for
     * @return ResponseEntity with List of BidResponse and HTTP 200 status
     */
    @GetMapping("/comitte/{comitteId}")
    public ResponseEntity<List<BidResponse>> getBidsByComitteId(@PathVariable Long comitteId) {
        log.info("Fetching bids for comitte ID: {}", comitteId);
        List<BidResponse> bids = bidService.getBidsByComitteId(comitteId);
        return ResponseEntity.ok(bids);
    }

    /**
     * Retrieve all bids for committees where a member belongs.
     * 
     * <p>This endpoint fetches all bids from committees where the specified member
     * is a participant. The response includes complete bid details with receiversList
     * populated in a single query for optimal performance.</p>
     * 
     * <p><b>Performance Note:</b> This endpoint uses JOIN FETCH to eagerly load
     * related collections, minimizing database roundtrips while retrieving
     * comprehensive bid information.</p>
     * 
     * <p><b>Security Considerations:</b>
     * <ul>
     *   <li>Ensure proper authorization - members should only access their own data</li>
     *   <li>Consider implementing pagination for members with many committee memberships</li>
     *   <li>Validate memberId to prevent unauthorized data access</li>
     * </ul>
     * </p>
     * 
     * @param memberId the ID of the member to retrieve committee bids for
     * @return ResponseEntity with List of BidResponse (including receiversList) and HTTP 200 status
     */
    @GetMapping("/member/{memberId}/committee-bids")
    public ResponseEntity<List<BidResponse>> getBidsForMemberCommittees(@PathVariable Long memberId) {
        log.info("Fetching bids for all committees where member ID {} belongs", memberId);
        List<BidResponse> bids = bidService.getBidsForMemberCommittees(memberId);
        return ResponseEntity.ok(bids);
    }
}
