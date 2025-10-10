package com.ls.auth.controller;

import com.ls.auth.model.request.PasswordResetRequest;
import com.ls.auth.model.request.PasswordUpdateRequest;
import com.ls.auth.service.ForgetPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/password")
@RequiredArgsConstructor
public class PasswordResetController {

    private final ForgetPasswordService forgetPasswordService;

    @PostMapping("/request-reset")
    public ResponseEntity<String> requestPasswordReset(@RequestBody PasswordResetRequest request) {
        forgetPasswordService.requestPasswordReset(request);
        return ResponseEntity.ok("Password reset email sent successfully.");
    }

    @PostMapping("/reset")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordUpdateRequest request) {
        forgetPasswordService.resetPassword(request);
        return ResponseEntity.ok("Password has been reset successfully.");
    }
}

