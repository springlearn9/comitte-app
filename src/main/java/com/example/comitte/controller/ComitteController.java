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
    private final ComitteService comitteService;

    @PostMapping
    public ResponseEntity<ComitteDto> create(@Valid @RequestBody ComitteCreateDto dto) {
        return ResponseEntity.status(201).body(comitteService.create(dto));
    }

    @GetMapping
    public ResponseEntity<Page<ComitteDto>> list(Pageable p) {
        return ResponseEntity.ok(comitteService.list(p));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComitteDto> get(@PathVariable Long id) {
        return ResponseEntity.ok(comitteService.get(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComitteDto> update(@PathVariable Long id, @Valid @RequestBody ComitteCreateDto dto) {
        return ResponseEntity.ok(comitteService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        comitteService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/assign-members")
    public ResponseEntity<ComitteDto> assign(@PathVariable Long id, @RequestBody java.util.List<Long> memberIds) {
        return ResponseEntity.ok(comitteService.assignMembers(id, memberIds));
    }
}
