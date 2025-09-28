package com.example.comitte.controller;

import com.example.comitte.model.dto.member.MemberCreateDto;
import com.example.comitte.model.dto.member.MemberDto;
import com.example.comitte.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<MemberDto> create(@Valid @RequestBody MemberCreateDto dto) {
        return ResponseEntity.status(201).body(memberService.create(dto));
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberDto> get(@PathVariable Long memberId) {
        return ResponseEntity.ok(memberService.get(memberId));
    }

    @PutMapping("/{memberId}")
    public ResponseEntity<MemberDto> update(@PathVariable Long memberId, @Valid @RequestBody MemberCreateDto dto) {
        return ResponseEntity.ok(memberService.update(memberId, dto));
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<?> delete(@PathVariable Long memberId) {
        memberService.delete(memberId);
        return ResponseEntity.noContent().build();
    }
}
