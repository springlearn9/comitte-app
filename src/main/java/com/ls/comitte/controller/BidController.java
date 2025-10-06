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

@RestController
@RequestMapping("/api/bids")
@RequiredArgsConstructor
@Slf4j
public class BidController {
    private final BidService bidService;

    @PostMapping
    public ResponseEntity<BidResponse> create(@Valid @RequestBody BidRequest bidRequest) {
        log.info("Creating bid with data: {}", bidRequest);
        BidResponse response = bidService.create(bidRequest);
        log.info("Bid created with ID: {}", response.bidId());
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{bidId}")
    public ResponseEntity<BidResponse> get(@PathVariable Long bidId) {
        log.info("Fetching bid with ID: {}", bidId);
        BidResponse response = bidService.get(bidId);
        log.info("Fetched bid: {}", response);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{bidId}")
    public ResponseEntity<BidResponse> update(@PathVariable Long bidId, @Valid @RequestBody BidRequest bidRequest) {
        log.info("Updating bid with ID: {} using data: {}", bidId, bidRequest);
        BidResponse response = bidService.update(bidId, bidRequest);
        log.info("Updated bid: {}", response);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{bidId}")
    public ResponseEntity<?> delete(@PathVariable Long bidId) {
        log.info("Deleting bid with ID: {}", bidId);
        bidService.delete(bidId);
        log.info("Deleted bid with ID: {}", bidId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{bidId}/place-bid")
    public ResponseEntity<BidResponse> placeBid(@PathVariable Long bidId, @RequestBody BidItem dto) {
        log.info("Placing bid on bid ID: {} with data: {}", bidId, dto);
        BidResponse response = bidService.placeBid(bidId, dto);
        log.info("Placed bid: {}", response);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{bidId}/history")
    public ResponseEntity<BidResponse> history(@PathVariable Long bidId) {
        log.info("Fetching history for bid ID: {}", bidId);
        BidResponse response = bidService.get(bidId);
        log.info("Fetched history: {}", response);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/comitte/{comitteId}")
    public ResponseEntity<List<BidResponse>> getBidsByComitteId(@PathVariable Long comitteId) {
        log.info("Fetching bids for comitte ID: {}", comitteId);
        List<BidResponse> bids = bidService.getBidsByComitteId(comitteId);
        log.info("Fetched bids: {}", bids);
        return ResponseEntity.ok(bids);
    }
}
