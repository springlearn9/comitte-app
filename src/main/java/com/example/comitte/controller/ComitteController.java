package com.example.comitte.controller;

import com.example.comitte.model.dto.comitte.ComitteCreateDto;
import com.example.comitte.model.dto.comitte.ComitteDto;
import com.example.comitte.service.ComitteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/comittes")
@RequiredArgsConstructor
public class ComitteController {
    private final ComitteService service;

    @PostMapping
    public ResponseEntity<ComitteDto> create(@Valid @RequestBody ComitteCreateDto dto) {
        return ResponseEntity.status(201).body(service.create(dto));
    }

    @GetMapping
    public ResponseEntity<Page<ComitteDto>> list(Pageable p) {
        return ResponseEntity.ok(service.list(p));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComitteDto> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComitteDto> update(@PathVariable Long id, @Valid @RequestBody ComitteCreateDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/assign-members")
    public ResponseEntity<ComitteDto> assign(@PathVariable Long id, @RequestBody java.util.List<Long> memberIds) {
        return ResponseEntity.ok(service.assignMembers(id, memberIds));
    }
}
