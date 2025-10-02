package com.example.comitte.controller;

import com.example.comitte.model.dto.map.ComitteMemberMapCreateDto;
import com.example.comitte.model.dto.map.ComitteMemberMapDto;
import com.example.comitte.service.ComitteMemberMapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/comitte-member-map")
@RequiredArgsConstructor
public class ComitteMemberMapController {
    private final ComitteMemberMapService service;

    @PostMapping
    public ResponseEntity<ComitteMemberMapDto> create(@Valid @RequestBody ComitteMemberMapCreateDto dto) {
        return ResponseEntity.status(201).body(service.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComitteMemberMapDto> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComitteMemberMapDto> update(@PathVariable Long id, @Valid @RequestBody ComitteMemberMapCreateDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
