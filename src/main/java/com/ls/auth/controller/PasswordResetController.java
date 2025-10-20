package com.ls.auth.controller;

import com.ls.auth.model.request.PasswordResetRequest;
import com.ls.auth.model.request.PasswordUpdateRequest;
import com.ls.auth.service.ForgetPasswordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for password reset operations.
 * 
 * Provides endpoints for requesting password reset tokens and resetting passwords.
 * Uses token-based authentication for password reset workflow.
 * These endpoints are public but secured with token validation.
 * 
 * Base Path: /api/password
 */
@RestController
@RequestMapping("/api/password")
@RequiredArgsConstructor
@Slf4j
public class PasswordResetController {

    private final ForgetPasswordService forgetPasswordService;

    /**
     * Requests a password reset token.
     * 
     * Purpose: Initiates password reset workflow by sending token to user's email
     * Request: PasswordResetRequest (JSON body) with email or username
     * Response: Success message confirming email sent
     * Status Codes:
     *   - 200: Reset email sent successfully
     *   - 400: Invalid request format
     *   - 404: User not found (may return 200 for security)
     *   - 500: Server error (email service failure)
     * 
     * Security: Public endpoint; always returns 200 to avoid user enumeration
     * Performance: Database query for user lookup and token generation
     * Error Mapping: Service silently handles user not found for security
     * Developer Notes: Token expires after configured time; rate limit this endpoint
     * 
     * @param request the password reset request with user identifier
     * @return ResponseEntity with success message and 200 status
     */
    @PostMapping("/request-reset")
    public ResponseEntity<String> requestPasswordReset(@RequestBody PasswordResetRequest request) {
        log.info("Password reset requested for email/username");
        forgetPasswordService.requestPasswordReset(request);
        log.info("Password reset email sent if user exists");
        return ResponseEntity.ok("Password reset email sent.");
    }

    /**
     * Resets user password using token.
     * 
     * Purpose: Validates reset token and updates user password
     * Request: PasswordUpdateRequest (JSON body) with token and new password
     * Response: Success message confirming password reset
     * Status Codes:
     *   - 200: Password reset successfully
     *   - 400: Invalid token or password format
     *   - 401: Token expired or invalid
     *   - 500: Server error
     * 
     * Validation: Service validates token, expiration, and password strength
     * Security: Public endpoint; token is single-use and time-limited
     * Performance: Database query for token validation and password update
     * Error Mapping: Service throws 401 for invalid/expired tokens
     * Developer Notes: New password is hashed before storage; token is invalidated after use
     * 
     * @param request the password update request with token and new password
     * @return ResponseEntity with success message and 200 status
     */
    @PostMapping("/reset")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordUpdateRequest request) {
        log.info("Password reset attempt with token");
        forgetPasswordService.resetPassword(request);
        log.info("Password reset successful");
        return ResponseEntity.ok("Password reset successful.");
    }
}

