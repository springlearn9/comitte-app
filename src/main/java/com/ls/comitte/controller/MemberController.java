package com.ls.comitte.controller;

import com.ls.comitte.model.request.MemberRequest;
import com.ls.comitte.model.response.MemberResponse;
import com.ls.comitte.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; // Import SLF4J for logging
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Slf4j // Add SLF4J annotation for logging
public class MemberController {
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<MemberResponse> create(@Valid @RequestBody MemberRequest dto) {
        log.info("Creating member with data: {}", dto);
        MemberResponse response = memberService.create(dto);
        log.info("Member created with ID: {}", response.getMemberId());
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponse> get(@PathVariable Long memberId) {
        log.info("Fetching member with ID: {}", memberId);
        MemberResponse response = memberService.get(memberId);
        log.info("Fetched member: {}", response);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{memberId}")
    public ResponseEntity<MemberResponse> update(@PathVariable Long memberId, @Valid @RequestBody MemberRequest dto) {
        log.info("Updating member with ID: {} using data: {}", memberId, dto);
        MemberResponse response = memberService.update(memberId, dto);
        log.info("Updated member: {}", response);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<?> delete(@PathVariable Long memberId) {
        log.info("Deleting member with ID: {}", memberId);
        memberService.delete(memberId);
        log.info("Deleted member with ID: {}", memberId);
        return ResponseEntity.noContent().build();
    }
}
