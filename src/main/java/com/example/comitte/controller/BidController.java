package com.example.comitte.controller;

import com.example.comitte.model.dto.bid.BidItemDto;
import com.example.comitte.model.dto.bid.ComitteBidCreateDto;
import com.example.comitte.model.dto.bid.ComitteBidDto;
import com.example.comitte.service.BidService;
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
    public ResponseEntity<ComitteBidDto> create(@Valid @RequestBody ComitteBidCreateDto dto) {
        log.info("Creating bid with data: {}", dto);
        ComitteBidDto response = bidService.create(dto);
        log.info("Bid created with ID: {}", response.getId());
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{bidId}")
    public ResponseEntity<ComitteBidDto> get(@PathVariable Long bidId) {
        log.info("Fetching bid with ID: {}", bidId);
        ComitteBidDto response = bidService.get(bidId);
        log.info("Fetched bid: {}", response);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{bidId}")
    public ResponseEntity<ComitteBidDto> update(@PathVariable Long bidId, @Valid @RequestBody ComitteBidCreateDto dto) {
        log.info("Updating bid with ID: {} using data: {}", bidId, dto);
        ComitteBidDto response = bidService.update(bidId, dto);
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
    public ResponseEntity<ComitteBidDto> placeBid(@PathVariable Long bidId, @RequestBody BidItemDto dto) {
        log.info("Placing bid on bid ID: {} with data: {}", bidId, dto);
        ComitteBidDto response = bidService.placeBid(bidId, dto);
        log.info("Placed bid: {}", response);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<ComitteBidDto> history(@PathVariable Long bidId) {
        log.info("Fetching history for bid ID: {}", bidId);
        ComitteBidDto response = bidService.get(bidId);
        log.info("Fetched history: {}", response);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/comitte/{comitteId}")
    public ResponseEntity<List<ComitteBidDto>> getBidsByComitteId(@PathVariable Long comitteId) {
        log.info("Fetching bids for comitte ID: {}", comitteId);
        List<ComitteBidDto> bids = bidService.getBidsByComitteId(comitteId);
        log.info("Fetched bids: {}", bids);
        return ResponseEntity.ok(bids);
    }
}
