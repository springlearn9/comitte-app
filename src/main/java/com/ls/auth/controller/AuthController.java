package com.ls.auth.controller;

import com.ls.auth.model.request.LoginRequest;
import com.ls.auth.model.request.RegisterRequest;
import com.ls.auth.model.response.LoginResponse;
import com.ls.comitte.model.response.MemberResponse;
import com.ls.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * REST controller for authentication operations.
 * 
 * Provides endpoints for user registration and login.
 * Handles JWT token generation for authenticated sessions.
 * No authentication required for these endpoints.
 * 
 * Base Path: /api/auth
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;

    /**
     * Registers a new user.
     * 
     * Purpose: Creates a new user account with member profile
     * Request: RegisterRequest (JSON body) - validated
     * Response: MemberResponse with created member details (excluding password)
     * Status Codes:
     *   - 201: User registered successfully
     *   - 400: Invalid input data (validation failure, duplicate username/email)
     *   - 500: Server error
     * 
     * Validation: Enforced via @Valid annotation; checks username, email, password strength
     * Security: Public endpoint; password is hashed before storage
     * Performance: Database insert with unique constraint checks
     * Error Mapping: Service validates unique constraints on username and email
     * Developer Notes: Consider email verification workflow for production
     * 
     * @param dto the registration request containing user credentials and profile
     * @param uriBuilder URI components builder for resource location
     * @return ResponseEntity with created member details and 201 status
     */
    @PostMapping("/register")
    public ResponseEntity<MemberResponse> register(@Valid @RequestBody RegisterRequest dto, UriComponentsBuilder uriBuilder) {
        log.info("User registration initiated for username: {}", dto.getUsername());
        MemberResponse memberResponse = authService.register(dto);
        log.info("User registered successfully with member ID: {}", memberResponse.memberId());
        return new ResponseEntity<>(memberResponse, HttpStatus.CREATED);
    }

    /**
     * Authenticates a user and generates JWT token.
     * 
     * Purpose: Validates user credentials and issues JWT access token
     * Request: LoginRequest (JSON body) - validated
     * Response: LoginResponse with JWT token and user details
     * Status Codes:
     *   - 200: Login successful, token issued
     *   - 400: Invalid request format (validation failure)
     *   - 401: Invalid credentials (username or password incorrect)
     *   - 500: Server error
     * 
     * Validation: Enforced via @Valid annotation on credentials
     * Security: Public endpoint; uses BCrypt for password verification
     * Performance: Database query for user lookup and password verification
     * Error Mapping: Service throws 401 for invalid credentials
     * Developer Notes: Token expiration and refresh logic handled by AuthService; avoid logging sensitive data
     * 
     * @param loginRequest the login credentials
     * @return ResponseEntity with JWT token and user details
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Login attempt for user: {}", loginRequest.getUsernameOrEmail());
        LoginResponse response = authService.login(loginRequest);
        log.info("Login successful for user: {}", loginRequest.getUsernameOrEmail());
        return ResponseEntity.ok(response);
    }
}
