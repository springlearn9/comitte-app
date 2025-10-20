package com.ls.auth.controller;

import com.ls.auth.model.request.PasswordResetRequest;
import com.ls.auth.model.request.PasswordUpdateRequest;
import com.ls.auth.service.ForgetPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Password reset endpoints for initiating and completing password reset flows.
 * 
 * This controller manages the password recovery process through a two-step workflow:
 * 1. Request a password reset (generates OTP/token and sends email)
 * 2. Complete the reset using the token/OTP and new password
 * 
 * Developer notes:
 * - Always use HTTPS for these endpoints to protect reset tokens and passwords in transit.
 * - Reset tokens/OTPs should have limited validity (e.g., 15-30 minutes) to reduce security risks.
 * - Do not log or return tokens, OTPs, or passwords in responses or logs.
 * - Email delivery may be asynchronous; consider implementing retry mechanisms for failed sends.
 * - Rate limiting should be applied to prevent abuse (e.g., spamming reset emails).
 * - Consider adding CAPTCHA for the request-reset endpoint to prevent automated abuse.
 * - Tokens should be single-use and invalidated after successful password reset.
 */
@RestController
@RequestMapping("/api/password")
@RequiredArgsConstructor
public class PasswordResetController {

    private final ForgetPasswordService forgetPasswordService;

    /**
     * Initiate password reset process by sending a reset token/OTP via email.
     * 
     * Validates the user exists and generates a time-limited reset token (or OTP),
     * which is sent to the user's registered email address. This endpoint is designed
     * to be safe to call even if the user doesn't exist (to prevent user enumeration).
     * 
     * Request: PasswordResetRequest containing usernameOrEmail
     * Response: 200 OK with success message (always returned, regardless of user existence)
     * 
     * HTTP Status Codes:
     * - 200 OK: Request processed (email sent if user exists)
     * - 400 Bad Request: Validation failure (missing usernameOrEmail)
     * - 500 Internal Server Error: Email service failure or unexpected error
     * 
     * Developer notes:
     * - Always return 200 OK to prevent user enumeration attacks (don't reveal if user exists).
     * - Token/OTP generation should use cryptographically secure random values.
     * - Tokens are stored in the database with expiration timestamp (e.g., 15-30 minutes).
     * - Email is sent asynchronously via the mail service (consider retry logic for failures).
     * - Multiple reset requests should invalidate previous tokens for the same user.
     * - Rate limiting is critical: limit requests per IP and per email to prevent abuse.
     * - Log reset requests with user identifiers (not tokens) for audit purposes.
     * - Consider implementing CAPTCHA verification to prevent automated attacks.
     * 
     * @param request the password reset request containing username or email
     * @return ResponseEntity with HTTP 200 and a generic success message
     */
    @PostMapping("/request-reset")
    public ResponseEntity<String> requestPasswordReset(@RequestBody PasswordResetRequest request) {
        forgetPasswordService.requestPasswordReset(request);
        return ResponseEntity.ok("Password reset email sent.");
    }

    /**
     * Complete password reset using the provided token/OTP and new password.
     * 
     * Validates the reset token/OTP, verifies it hasn't expired, and updates the user's
     * password with the new hashed password. The token is invalidated after successful reset.
     * 
     * Request: PasswordUpdateRequest containing token, usernameOrEmail, otp, and newPassword
     * Response: 200 OK with success message
     * 
     * HTTP Status Codes:
     * - 200 OK: Password successfully reset
     * - 400 Bad Request: Validation failure, invalid token/OTP, or token expired
     * - 401 Unauthorized: Token doesn't match user or has been invalidated
     * - 500 Internal Server Error: Unexpected error during password update
     * 
     * Developer notes:
     * - Token/OTP validation includes: existence, expiration check, and user match.
     * - The new password is hashed using BCrypt before storage (never store plain-text passwords).
     * - The token is marked as used/deleted after successful password reset to prevent reuse.
     * - Failed reset attempts should be logged for security monitoring (without exposing tokens).
     * - Consider enforcing password complexity requirements (length, character types).
     * - The token verification should be constant-time to prevent timing attacks.
     * - After successful reset, consider invalidating existing user sessions/tokens for security.
     * - May want to send a confirmation email after successful password change.
     * 
     * @param request the password update request containing token, credentials, and new password
     * @return ResponseEntity with HTTP 200 and success message
     */
    @PostMapping("/reset")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordUpdateRequest request) {
        forgetPasswordService.resetPassword(request);
        return ResponseEntity.ok("Password reset successful.");
    }
}

