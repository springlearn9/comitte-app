package com.example.comitte.controller;

import com.example.comitte.model.dto.bid.ComitteBidDto;
import com.example.comitte.model.dto.comitte.ComitteCreateDto;
import com.example.comitte.model.dto.comitte.ComitteDto;
import com.example.comitte.service.BidService;
import com.example.comitte.service.ComitteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comittes")
@RequiredArgsConstructor
public class ComitteController {
    private final ComitteService comitteService;
    private final BidService bidService;

    @PostMapping
    public ResponseEntity<ComitteDto> create(@Valid @RequestBody ComitteCreateDto dto) {
        return ResponseEntity.status(201).body(comitteService.create(dto));
    }

    @GetMapping("/{comitteId}")
    public ResponseEntity<ComitteDto> get(@PathVariable Long comitteId) {
        return ResponseEntity.ok(comitteService.get(comitteId));
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<ComitteDto>> getMemberComittes(@PathVariable Long memberId) {
        return ResponseEntity.ok(comitteService.getMemberComittes(memberId));
    }

    @PutMapping("/{comitteId}")
    public ResponseEntity<ComitteDto> update(@PathVariable Long comitteId, @Valid @RequestBody ComitteCreateDto dto) {
        return ResponseEntity.ok(comitteService.update(comitteId, dto));
    }

    @DeleteMapping("/{comitteId}")
    public ResponseEntity<?> delete(@PathVariable Long comitteId) {
        comitteService.delete(comitteId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{comitteId}/assign-members")
    public ResponseEntity<ComitteDto> assign(@PathVariable Long id, @RequestBody java.util.List<Long> memberIds) {
        return ResponseEntity.ok(comitteService.assignMembers(id, memberIds));
    }

    // Get all bids for a comitte
    @GetMapping("/{comitteId}/bids")
    public ResponseEntity<List<ComitteBidDto>> getBidsByComitteId(@PathVariable Long comitteId) {
        List<ComitteBidDto> bids = bidService.getBidsByComitteId(comitteId);
        return ResponseEntity.ok(bids);
    }
}
