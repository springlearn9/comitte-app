package com.ls.comitte.controller;

import com.ls.comitte.model.request.ComitteMemberMapRequest;
import com.ls.comitte.model.response.ComitteMemberMapResponse;
import com.ls.comitte.service.ComitteMemberMapService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; // Import SLF4J for logging
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/comitte-member-map")
@RequiredArgsConstructor
@Slf4j // Add SLF4J annotation for logging
public class ComitteMemberMapController {
    private final ComitteMemberMapService service;

    @PostMapping
    public ResponseEntity<ComitteMemberMapResponse> create(@Valid @RequestBody ComitteMemberMapRequest dto) {
        log.info("Creating comitte member map with data: {}", dto);
        ComitteMemberMapResponse response = service.create(dto);
        log.info("Comitte member map created with ID: {}", response.getId());
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComitteMemberMapResponse> get(@PathVariable Long id) {
        log.info("Fetching comitte member map with ID: {}", id);
        ComitteMemberMapResponse response = service.get(id);
        log.info("Fetched comitte member map: {}", response);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComitteMemberMapResponse> update(@PathVariable Long id, @Valid @RequestBody ComitteMemberMapRequest dto) {
        log.info("Updating comitte member map with ID: {} using data: {}", id, dto);
        ComitteMemberMapResponse response = service.update(id, dto);
        log.info("Updated comitte member map: {}", response);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        log.info("Deleting comitte member map with ID: {}", id);
        service.delete(id);
        log.info("Deleted comitte member map with ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}
