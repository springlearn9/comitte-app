package com.ls.comitte.controller;

import com.ls.comitte.model.response.BidResponse;
import com.ls.comitte.model.request.ComitteRequest;
import com.ls.comitte.model.response.ComitteResponse;
import com.ls.comitte.model.response.MemberResponse;
import com.ls.comitte.service.BidService;
import com.ls.comitte.service.ComitteService;
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
    public ResponseEntity<ComitteResponse> create(@Valid @RequestBody ComitteRequest dto) {
        log.info("Creating comitte with data: {}", dto);
        ComitteResponse response = comitteService.create(dto);
        log.info("Comitte created with ID: {}", response.comitteId());
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{comitteId}")
    public ResponseEntity<ComitteResponse> get(@PathVariable Long comitteId) {
        log.info("Fetching comitte with ID: {}", comitteId);
        ComitteResponse response = comitteService.get(comitteId);
        log.info("Fetched comitte: {}", response);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<ComitteResponse>> getMemberComittes(@PathVariable Long memberId) {
        log.info("Fetching comittes for member ID: {}", memberId);
        List<ComitteResponse> comittes = comitteService.getMemberComittes(memberId);
        log.info("Fetched comittes: {}", comittes);
        return ResponseEntity.ok(comittes);
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<ComitteResponse>> getOwnerComittes(@PathVariable Long ownerId) {
        log.info("Fetching comittes for owner ID: {}", ownerId);
        List<ComitteResponse> comittes = comitteService.getOwnerComittes(ownerId);
        log.info("Fetched comittes: {}", comittes);
        return ResponseEntity.ok(comittes);
    }

    @PutMapping("/{comitteId}")
    public ResponseEntity<ComitteResponse> update(@PathVariable Long comitteId, @Valid @RequestBody ComitteRequest dto) {
        log.info("Updating comitte with ID: {} using data: {}", comitteId, dto);
        ComitteResponse response = comitteService.update(comitteId, dto);
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
    public ResponseEntity<ComitteResponse> assign(@PathVariable Long comitteId, @RequestBody List<Long> memberIds) {
        log.info("Assigning members to comitte ID: {} with member IDs: {}", comitteId, memberIds);
        ComitteResponse response = comitteService.assignMembers(comitteId, memberIds);
        log.info("Assigned members to comitte: {}", response);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{comitteId}/bids")
    public ResponseEntity<List<BidResponse>> getBidsByComitteId(@PathVariable Long comitteId) {
        log.info("Fetching bids for comitte ID: {}", comitteId);
        List<BidResponse> bids = bidService.getBidsByComitteId(comitteId);
        log.info("Fetched bids: {}", bids);
        return ResponseEntity.ok(bids);
    }

    @GetMapping("/{comitteId}/members")
    public ResponseEntity<List<MemberResponse>> getAllAssociatedMembers(@PathVariable Long comitteId) {
        List<MemberResponse> members = comitteService.getAllAssociatedMembers(comitteId);
        return ResponseEntity.ok(members);
    }
}
