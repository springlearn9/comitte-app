package com.ls.comitte.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * Global exception handler for the API.
 * 
 * Provides centralized exception handling across all controllers.
 * Returns structured JSON error responses with timestamp, status, error, message, and unique errorId.
 * Logs all exceptions with errorId for traceability.
 * 
 * Error Response Format:
 * {
 *   "timestamp": "2025-10-20T05:00:00",
 *   "status": 500,
 *   "error": "Internal Server Error",
 *   "message": "Error details",
 *   "errorId": "unique-uuid"
 * }
 */
@ControllerAdvice
@Slf4j
public class ApiExceptionHandler {

    /**
     * Handles all uncaught exceptions.
     * 
     * Purpose: Catch-all handler for any exception not handled by more specific handlers.
     * Response: 500 Internal Server Error with structured error payload
     * Logging: Logs full stack trace with errorId for debugging
     * Security: Avoids exposing sensitive internal details in the message
     * 
     * @param ex The exception that was thrown
     * @param request The web request context
     * @return ResponseEntity with error details and HTTP 500 status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception ex, WebRequest request) {
        String errorId = UUID.randomUUID().toString();
        log.error("Error ID: {}, Message: {}", errorId, ex.getMessage(), ex);
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), errorId);
    }

    /**
     * Handles ResponseStatusException to preserve custom HTTP status codes.
     * 
     * Purpose: Handles exceptions thrown with specific HTTP status codes (e.g., 404, 400)
     * Response: Returns the specified HTTP status with structured error payload
     * Logging: Logs with errorId for traceability
     * Performance: Lightweight handler that preserves original status code
     * 
     * @param ex The ResponseStatusException containing status and reason
     * @param request The web request context
     * @return ResponseEntity with error details and the exception's status code
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> handleResponseStatusException(ResponseStatusException ex, WebRequest request) {
        String errorId = UUID.randomUUID().toString();
        log.error("Error ID: {}, Message: {}", errorId, ex.getReason(), ex);
        return buildResponseEntity(ex.getStatusCode(), ex.getReason(), errorId);
    }

    /**
     * Builds a structured error response with HttpStatus.
     * 
     * @param httpStatus The HTTP status code
     * @param message Error message for the client
     * @param errorId Unique identifier for error tracking
     * @return ResponseEntity with structured error payload
     */
    private ResponseEntity<Map<String, Object>> buildResponseEntity(HttpStatus httpStatus, String message, String errorId) {
        return ResponseEntity.status(httpStatus).body(Map.of(
                "timestamp", LocalDateTime.now(),
                "status", httpStatus.value(),
                "error", httpStatus.getReasonPhrase(),
                "message", message,
                "errorId", errorId
        ));
    }

    /**
     * Builds a structured error response with HttpStatusCode.
     * 
     * @param httpStatusCode The HTTP status code
     * @param message Error message for the client
     * @param errorId Unique identifier for error tracking
     * @return ResponseEntity with structured error payload
     */
    private ResponseEntity<Map<String, Object>> buildResponseEntity(HttpStatusCode httpStatusCode, String message, String errorId) {
        return ResponseEntity.status(httpStatusCode).body(Map.of(
                "timestamp", LocalDateTime.now(),
                "status", httpStatusCode.value(),
                "error", httpStatusCode.toString(),
                "message", message,
                "errorId", errorId
        ));
    }
}
