package com.ls.construction.controller;

import com.ls.construction.model.request.PaymentDetailsRequest;
import com.ls.construction.model.response.PaymentDetailsResponse;
import com.ls.construction.service.PaymentDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

/**
 * REST controller for managing Payment Details resources.
 * 
 * <p>This controller provides CRUD operations for construction payment records.</p>
 */
@RestController
@RequestMapping("/api/payment-details")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Payment Details", description = "Payment management APIs")
@SecurityRequirement(name = "bearerAuth")
public class PaymentDetailsController {
    private final PaymentDetailsService paymentDetailsService;

    @PostMapping
    @Operation(summary = "Create a new payment entry", description = "Creates a new payment details entry for a project")
    public ResponseEntity<PaymentDetailsResponse> create(@Valid @RequestBody PaymentDetailsRequest dto) {
        log.info("Creating payment details entry");
        PaymentDetailsResponse response = paymentDetailsService.create(dto);
        log.info("Payment details entry created with ID: {}", response.paymentId());
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{paymentId}")
    @Operation(summary = "Get payment details by ID", description = "Retrieves a specific payment details entry by its ID")
    public ResponseEntity<PaymentDetailsResponse> get(@PathVariable Long paymentId) {
        log.info("Getting payment details with ID: {}", paymentId);
        return ResponseEntity.ok(paymentDetailsService.get(paymentId));
    }

    @GetMapping
    @Operation(summary = "Get all payment details", description = "Retrieves all payment details entries")
    public ResponseEntity<List<PaymentDetailsResponse>> getAll() {
        log.info("Getting all payment details");
        return ResponseEntity.ok(paymentDetailsService.getAll());
    }

    @GetMapping("/project/{projectId}")
    @Operation(summary = "Get payment details by project", description = "Retrieves all payment details for a specific project")
    public ResponseEntity<List<PaymentDetailsResponse>> getByProject(@PathVariable Long projectId) {
        log.info("Getting payment details for project ID: {}", projectId);
        return ResponseEntity.ok(paymentDetailsService.getByProject(projectId));
    }

    @PutMapping("/{paymentId}")
    @Operation(summary = "Update payment details", description = "Updates an existing payment details entry")
    public ResponseEntity<PaymentDetailsResponse> update(@PathVariable Long paymentId, @Valid @RequestBody PaymentDetailsRequest dto) {
        log.info("Updating payment details with ID: {}", paymentId);
        return ResponseEntity.ok(paymentDetailsService.update(paymentId, dto));
    }

    @DeleteMapping("/{paymentId}")
    @Operation(summary = "Delete payment details", description = "Deletes a payment details entry by ID")
    public ResponseEntity<Void> delete(@PathVariable Long paymentId) {
        log.info("Deleting payment details with ID: {}", paymentId);
        paymentDetailsService.delete(paymentId);
        return ResponseEntity.noContent().build();
    }
}
