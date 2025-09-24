package com.example.comitte.controller;

import com.example.comitte.model.dto.bid.BidItemDto;
import com.example.comitte.model.dto.bid.ComitteBidCreateDto;
import com.example.comitte.model.dto.bid.ComitteBidDto;
import com.example.comitte.service.ComitteBidService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/comitte-bids")
@RequiredArgsConstructor
public class BidController {
    private final ComitteBidService service;

    @PostMapping
    public ResponseEntity<ComitteBidDto> create(@Valid @RequestBody ComitteBidCreateDto dto) {
        return ResponseEntity.status(201).body(service.create(dto));
    }

    @GetMapping
    public ResponseEntity<Page<ComitteBidDto>> list(Pageable p) {
        return ResponseEntity.ok(service.list(p));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComitteBidDto> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @GetMapping("/{comitteId}/bids")
    public ResponseEntity<List<ComitteBidDto>> getBidsByComitteId(@PathVariable Long comitteId) {
        List<ComitteBidDto> bids = service.getBidsByComitteId(comitteId);
        return ResponseEntity.ok(bids);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComitteBidDto> update(@PathVariable Long id, @Valid @RequestBody ComitteBidCreateDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/place-bid")
    public ResponseEntity<ComitteBidDto> placeBid(@PathVariable Long id, @RequestBody BidItemDto dto) {
        return ResponseEntity.ok(service.placeBid(id, dto));
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<ComitteBidDto> history(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }
}
