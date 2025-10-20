package com.ls.comitte.controller;

import com.ls.comitte.model.BidItem;
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
 * REST controller for managing bids.
 * 
 * Provides endpoints for creating, retrieving, updating, and deleting bids,
 * as well as placing bids on items and viewing bid history.
 * All endpoints require appropriate authentication and authorization.
 * 
 * Base Path: /api/bids
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
     * Purpose: Creates a new bid item in the system
     * Request: BidRequest (JSON body) - validated
     * Response: BidResponse with created bid details
     * Status Codes:
     *   - 201: Bid created successfully
     *   - 400: Invalid input data (validation failure)
     *   - 500: Server error
     * 
     * Validation: Enforced via @Valid annotation on BidRequest
     * Security: Requires authentication
     * Performance: Single database insert operation
     * 
     * @param bidRequest the bid creation request
     * @return ResponseEntity with created bid details and 201 status
     */
    @PostMapping
    public ResponseEntity<BidResponse> create(@Valid @RequestBody BidRequest bidRequest) {
        log.info("Creating bid with data: {}", bidRequest);
        BidResponse response = bidService.create(bidRequest);
        log.info("Bid created with ID: {}", response.bidId());
        return ResponseEntity.status(201).body(response);
    }

    /**
     * Retrieves a bid by ID.
     * 
     * Purpose: Fetches detailed information for a specific bid
     * Request: Path variable bidId
     * Response: BidResponse with bid details
     * Status Codes:
     *   - 200: Bid found and returned
     *   - 404: Bid not found
     *   - 500: Server error
     * 
     * Security: Requires authentication
     * Performance: Single database query by primary key
     * Error Mapping: Service throws ResponseStatusException with 404 if bid not found
     * 
     * @param bidId the ID of the bid to retrieve
     * @return ResponseEntity with bid details and 200 status
     */
    @GetMapping("/{bidId}")
    public ResponseEntity<BidResponse> get(@PathVariable Long bidId) {
        log.info("Fetching bid with ID: {}", bidId);
        BidResponse response = bidService.get(bidId);
        log.info("Fetched bid: {}", response);
        return ResponseEntity.ok(response);
    }

    /**
     * Updates an existing bid.
     * 
     * Purpose: Updates bid information for a specific bid ID
     * Request: Path variable bidId and BidRequest (JSON body) - validated
     * Response: BidResponse with updated bid details
     * Status Codes:
     *   - 200: Bid updated successfully
     *   - 400: Invalid input data (validation failure)
     *   - 404: Bid not found
     *   - 500: Server error
     * 
     * Validation: Enforced via @Valid annotation on BidRequest
     * Security: Requires authentication and ownership verification
     * Performance: Single database update operation
     * Error Mapping: Service throws ResponseStatusException with 404 if bid not found
     * 
     * @param bidId the ID of the bid to update
     * @param bidRequest the updated bid data
     * @return ResponseEntity with updated bid details and 200 status
     */
    @PutMapping("/{bidId}")
    public ResponseEntity<BidResponse> update(@PathVariable Long bidId, @Valid @RequestBody BidRequest bidRequest) {
        log.info("Updating bid with ID: {} using data: {}", bidId, bidRequest);
        BidResponse response = bidService.update(bidId, bidRequest);
        log.info("Updated bid: {}", response);
        return ResponseEntity.ok(response);
    }

    /**
     * Deletes a bid.
     * 
     * Purpose: Permanently removes a bid from the system
     * Request: Path variable bidId
     * Response: No content
     * Status Codes:
     *   - 204: Bid deleted successfully
     *   - 404: Bid not found
     *   - 500: Server error
     * 
     * Security: Requires authentication and ownership verification
     * Performance: Single database delete operation
     * Error Mapping: Service throws ResponseStatusException with 404 if bid not found
     * Developer Notes: This is a hard delete; consider soft delete for audit trails
     * 
     * @param bidId the ID of the bid to delete
     * @return ResponseEntity with 204 No Content status
     */
    @DeleteMapping("/{bidId}")
    public ResponseEntity<?> delete(@PathVariable Long bidId) {
        log.info("Deleting bid with ID: {}", bidId);
        bidService.delete(bidId);
        log.info("Deleted bid with ID: {}", bidId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Places a bid on an item.
     * 
     * Purpose: Records a member's bid on a specific bid item
     * Request: Path variable bidId and BidItem (JSON body)
     * Response: BidResponse with updated bid details including new bid item
     * Status Codes:
     *   - 200: Bid placed successfully
     *   - 400: Invalid bid data (e.g., bid amount too low)
     *   - 404: Bid not found
     *   - 500: Server error
     * 
     * Security: Requires authentication; member ID validated against authenticated user
     * Performance: Database update with bid history append
     * Error Mapping: Service validates bid rules (minimum amount, bid closure time, etc.)
     * Developer Notes: Concurrent bids are handled by service layer with optimistic locking
     * 
     * @param bidId the ID of the bid item
     * @param dto the bid details including member ID and amount
     * @return ResponseEntity with updated bid details and 200 status
     */
    @PostMapping("/{bidId}/place-bid")
    public ResponseEntity<BidResponse> placeBid(@PathVariable Long bidId, @RequestBody BidItem dto) {
        log.info("Placing bid on bid ID: {} with data: {}", bidId, dto);
        BidResponse response = bidService.placeBid(bidId, dto);
        log.info("Placed bid: {}", response);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves bid history.
     * 
     * Purpose: Fetches the complete bid history for a specific bid item
     * Request: Path variable bidId
     * Response: BidResponse including all historical bid items
     * Status Codes:
     *   - 200: History retrieved successfully
     *   - 404: Bid not found
     *   - 500: Server error
     * 
     * Security: Requires authentication; may restrict visibility based on user role
     * Performance: Single database query with bid items joined
     * Developer Notes: Currently delegates to get() method; consider separate endpoint for full history
     * 
     * @param bidId the ID of the bid
     * @return ResponseEntity with bid history and 200 status
     */
    @GetMapping("/{bidId}/history")
    public ResponseEntity<BidResponse> history(@PathVariable Long bidId) {
        log.info("Fetching history for bid ID: {}", bidId);
        BidResponse response = bidService.get(bidId);
        log.info("Fetched history: {}", response);
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
    @GetMapping("/comitte/{comitteId}")
    public ResponseEntity<List<BidResponse>> getBidsByComitteId(@PathVariable Long comitteId) {
        log.info("Fetching bids for comitte ID: {}", comitteId);
        List<BidResponse> bids = bidService.getBidsByComitteId(comitteId);
        log.info("Fetched bids: {}", bids);
        return ResponseEntity.ok(bids);
    }
}
