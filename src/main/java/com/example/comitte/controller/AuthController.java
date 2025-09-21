package com.example.comitte.controller;

import com.example.comitte.dto.auth.RegisterRequestDto;
import com.example.comitte.dto.auth.LoginRequestDto;
import com.example.comitte.dto.member.MemberDto;
import com.example.comitte.service.AuthService;
import com.example.comitte.dto.auth.LoginResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<MemberDto> register(@Valid @RequestBody RegisterRequestDto dto, UriComponentsBuilder uriBuilder) {
        MemberDto memberDto = authService.register(dto);
        return ResponseEntity.created(uriBuilder.path("/api/users/{id}").buildAndExpand(memberDto.getMemberId()).toUri()).body(memberDto);
    }

/*    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto dto) {
        LoginResponseDto res = authService.login(dto);
        return ResponseEntity.ok(res);
    }*/
}
