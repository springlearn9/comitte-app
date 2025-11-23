package com.ls.auth.controller;

import com.ls.auth.model.request.LoginRequest;
import com.ls.auth.model.request.RegisterRequest;
import com.ls.auth.model.response.LoginResponse;
import com.ls.auth.model.response.MemberResponse;
import com.ls.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Authentication endpoints for user registration and login.
 * 
 * This controller handles core authentication operations including new user registration
 * and user authentication. All endpoints should be accessed over HTTPS in production.
 * 
 * Developer notes:
 * - Always use HTTPS for these endpoints to protect sensitive data in transit.
 * - Do not log or return sensitive data (passwords, raw tokens). Logs should only include user IDs or operation markers.
 * - Input validation is enforced via Jakarta Validation annotations on DTOs.
 * - Security checks and business logic are delegated to the AuthService layer.
 * - Consider adding method-level security annotations (e.g., @PreAuthorize) when implementing role-based access control.
 * - Error responses follow standard HTTP status codes with appropriate error messages.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    /**
     * Register a new user account.
     * 
     * Creates a new user account with the provided credentials and profile information.
     * The password is hashed before storage, and a user profile is created.
     * 
     * Request: RegisterRequest containing username, email, password, and optional mobile
     * Response: 201 Created with MemberResponse containing user profile (excludes password)
     * 
     * HTTP Status Codes:
     * - 201 Created: User successfully registered
     * - 400 Bad Request: Validation failure (invalid email, blank fields)
     * - 409 Conflict: Username or email already exists
     * - 500 Internal Server Error: Unexpected error during registration
     * 
     * Developer notes:
     * - Password hashing is handled at the service/persistence layer using BCrypt.
     * - Never return raw passwords in the response.
     * - Validation is enforced via @Valid annotation and Jakarta Bean Validation constraints.
     * - Returns clear 4xx errors for validation failures or duplicate accounts.
     * - The service layer checks for existing username/email before creating the account.
     * 
     * @param dto the registration request containing user credentials and profile data (validated)
     * @param uriBuilder the URI builder for constructing location header (currently unused)
     * @return ResponseEntity with HTTP 201 and MemberResponse containing user profile
     */
    @PostMapping("/register")
    public ResponseEntity<MemberResponse> register(@Valid @RequestBody RegisterRequest dto, UriComponentsBuilder uriBuilder) {
        MemberResponse memberResponse = authService.register(dto);
        return new ResponseEntity<>(memberResponse, HttpStatus.CREATED);
    }

    /**
     * Authenticate a user and generate access token.
     * 
     * Validates user credentials (username/email and password) and returns an access token
     * upon successful authentication. The token should be included in subsequent requests
     * via the Authorization header.
     * 
     * Request: LoginRequest containing usernameOrEmail and password
     * Response: 200 OK with LoginResponse containing access token, token type, expiry, and user info
     * 
     * HTTP Status Codes:
     * - 200 OK: Authentication successful, token returned
     * - 400 Bad Request: Validation failure (missing credentials)
     * - 401 Unauthorized: Invalid credentials (username/email or password incorrect)
     * - 500 Internal Server Error: Unexpected error during authentication
     * 
     * Developer notes:
     * - Authentication is performed against the database using Spring Security authentication manager.
     * - Password verification uses BCrypt comparison (never store or log plain-text passwords).
     * - JWT tokens are generated with configurable expiration time.
     * - The response includes token metadata (type, expiration) and basic user information.
     * - Clients should store the token securely and include it in the Authorization header as "Bearer {token}".
     * - Failed login attempts should be logged for security monitoring (log only usernames, not passwords).
     * - Consider implementing rate limiting to prevent brute force attacks.
     * 
     * @param loginRequest the login credentials containing username/email and password (validated)
     * @return ResponseEntity with HTTP 200 and LoginResponse containing token and user details
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }
}
