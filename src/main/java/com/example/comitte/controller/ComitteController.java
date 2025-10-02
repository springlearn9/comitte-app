package com.example.comitte.controller;

import com.example.comitte.model.dto.bid.ComitteBidDto;
import com.example.comitte.model.dto.comitte.ComitteCreateDto;
import com.example.comitte.model.dto.comitte.ComitteDto;
import com.example.comitte.service.BidService;
import com.example.comitte.service.ComitteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/comittes")
@RequiredArgsConstructor
@Slf4j
public class ComitteController {
    private final ComitteService comitteService;
    private final BidService bidService;

    @PostMapping
    public ResponseEntity<ComitteDto> create(@Valid @RequestBody ComitteCreateDto dto) {
        log.info("Creating comitte with data: {}", dto);
        ComitteDto response = comitteService.create(dto);
        log.info("Comitte created with ID: {}", response.getComitteId());
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{comitteId}")
    public ResponseEntity<ComitteDto> get(@PathVariable Long comitteId) {
        log.info("Fetching comitte with ID: {}", comitteId);
        ComitteDto response = comitteService.get(comitteId);
        log.info("Fetched comitte: {}", response);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<ComitteDto>> getMemberComittes(@PathVariable Long memberId) {
        log.info("Fetching comittes for member ID: {}", memberId);
        List<ComitteDto> comittes = comitteService.getMemberComittes(memberId);
        log.info("Fetched comittes: {}", comittes);
        return ResponseEntity.ok(comittes);
    }

    @PutMapping("/{comitteId}")
    public ResponseEntity<ComitteDto> update(@PathVariable Long comitteId, @Valid @RequestBody ComitteCreateDto dto) {
        log.info("Updating comitte with ID: {} using data: {}", comitteId, dto);
        ComitteDto response = comitteService.update(comitteId, dto);
        log.info("Updated comitte: {}", response);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{comitteId}")
    public ResponseEntity<?> delete(@PathVariable Long comitteId) {
        log.info("Deleting comitte with ID: {}", comitteId);
        comitteService.delete(comitteId);
        log.info("Deleted comitte with ID: {}", comitteId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{comitteId}/assign-members")
    public ResponseEntity<ComitteDto> assign(@PathVariable Long id, @RequestBody List<Long> memberIds) {
        log.info("Assigning members to comitte ID: {} with member IDs: {}", id, memberIds);
        ComitteDto response = comitteService.assignMembers(id, memberIds);
        log.info("Assigned members to comitte: {}", response);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{comitteId}/bids")
    public ResponseEntity<List<ComitteBidDto>> getBidsByComitteId(@PathVariable Long comitteId) {
        log.info("Fetching bids for comitte ID: {}", comitteId);
        List<ComitteBidDto> bids = bidService.getBidsByComitteId(comitteId);
        log.info("Fetched bids: {}", bids);
        return ResponseEntity.ok(bids);
    }
}
