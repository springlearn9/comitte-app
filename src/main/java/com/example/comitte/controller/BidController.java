package com.example.comitte.controller;

import com.example.comitte.model.dto.bid.BidItemDto;
import com.example.comitte.model.dto.bid.ComitteBidCreateDto;
import com.example.comitte.model.dto.bid.ComitteBidDto;
import com.example.comitte.service.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/bids")
@RequiredArgsConstructor
public class BidController {
    private final BidService bidService;

    @PostMapping
    public ResponseEntity<ComitteBidDto> create(@Valid @RequestBody ComitteBidCreateDto dto) {
        return ResponseEntity.status(201).body(bidService.create(dto));
    }

    @GetMapping("/{bidId}")
    public ResponseEntity<ComitteBidDto> get(@PathVariable Long bidId) {
        return ResponseEntity.ok(bidService.get(bidId));
    }

    @PutMapping("/{bidId}")
    public ResponseEntity<ComitteBidDto> update(@PathVariable Long bidId, @Valid @RequestBody ComitteBidCreateDto dto) {
        return ResponseEntity.ok(bidService.update(bidId, dto));
    }

    @DeleteMapping("/{bidId}")
    public ResponseEntity<?> delete(@PathVariable Long bidId) {
        bidService.delete(bidId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{bidId}/place-bid")
    public ResponseEntity<ComitteBidDto> placeBid(@PathVariable Long bidId, @RequestBody BidItemDto dto) {
        return ResponseEntity.ok(bidService.placeBid(bidId, dto));
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<ComitteBidDto> history(@PathVariable Long bidId) {
        return ResponseEntity.ok(bidService.get(bidId));
    }

    @GetMapping("/comitte/{comitteId}")
    public ResponseEntity<List<ComitteBidDto>> getBidsByComitteId(@PathVariable Long comitteId) {
        List<ComitteBidDto> bids = bidService.getBidsByComitteId(comitteId);
        return ResponseEntity.ok(bids);
    }
}
