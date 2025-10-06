package com.ls.auth.controller;

import com.ls.auth.model.request.RegisterRequest;
import com.ls.comitte.model.response.MemberResponse;
import com.ls.auth.service.AuthService;
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
    public ResponseEntity<MemberResponse> register(@Valid @RequestBody RegisterRequest dto, UriComponentsBuilder uriBuilder) {
        MemberResponse memberResponse = authService.register(dto);
        return ResponseEntity.created(uriBuilder.path("/api/users/{bidId}").buildAndExpand(memberResponse.memberId()).toUri()).body(memberResponse);
    }

/*    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto dto) {
        LoginResponseDto res = authService.login(dto);
        return ResponseEntity.ok(res);
    }*/
}
