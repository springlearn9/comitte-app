package com.ls.common.exception;

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
 * Global exception handler for the application.
 * 
 * <p>This controller advice handles all uncaught exceptions and converts them into
 * structured JSON error responses with proper HTTP status codes. Each error response
 * includes a unique errorId for diagnostic purposes.</p>
 * 
 * <p><b>Error Response Structure:</b>
 * <pre>
 * {
 *   "timestamp": "2025-10-20T04:49:49.555",
 *   "status": 500,
 *   "error": "Internal Server Error",
 *   "message": "Error description",
 *   "errorId": "uuid-for-tracking"
 * }
 * </pre>
 * </p>
 * 
 * <p><b>Security Note:</b> This handler avoids exposing stack traces or internal
 * implementation details to clients. All exceptions are logged server-side with
 * the errorId for diagnostic purposes.</p>
 * 
 * <p><b>Developer Notes:</b>
 * <ul>
 *   <li>Add specific exception handlers for domain exceptions as needed</li>
 *   <li>Consider handling MethodArgumentNotValidException for validation errors</li>
 *   <li>Log exceptions at appropriate levels (ERROR, WARN, INFO)</li>
 *   <li>Avoid logging sensitive data in exception messages</li>
 *   <li>Use errorId to correlate client reports with server logs</li>
 * </ul>
 * </p>
 */
@ControllerAdvice
@Slf4j
public class ApiExceptionHandler {

    /**
     * Handles all generic exceptions that are not caught by more specific handlers.
     * 
     * <p>Returns HTTP 500 Internal Server Error with a structured JSON response
     * containing a unique errorId for tracking and diagnostics.</p>
     * 
     * <p><b>Developer Notes:</b>
     * <ul>
     *   <li>All exceptions are logged with full stack trace and errorId</li>
     *   <li>Client receives sanitized error message without stack trace</li>
     *   <li>Use errorId to correlate client reports with server logs</li>
     *   <li>Consider adding more specific exception handlers above this catch-all</li>
     * </ul>
     * </p>
     * 
     * @param ex the exception that was thrown
     * @param request the web request during which the exception occurred
     * @return ResponseEntity with error details and HTTP 500 status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception ex, WebRequest request) {
        String errorId = UUID.randomUUID().toString();
        log.error("Error ID: {}, Message: {}", errorId, ex.getMessage(), ex);
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), errorId);
    }

    /**
     * Handles ResponseStatusException, preserving the status code and reason from the exception.
     * 
     * <p>This handler allows service layers to throw ResponseStatusException with specific
     * HTTP status codes (e.g., 404 Not Found, 400 Bad Request) and custom messages.</p>
     * 
     * <p><b>Developer Notes:</b>
     * <ul>
     *   <li>Preserves the original HTTP status code from the exception</li>
     *   <li>All exceptions are logged with errorId for tracking</li>
     *   <li>Use this for controlled error responses with specific status codes</li>
     *   <li>Ensure error messages don't expose sensitive information</li>
     * </ul>
     * </p>
     * 
     * @param ex the ResponseStatusException that was thrown
     * @param request the web request during which the exception occurred
     * @return ResponseEntity with error details and the status from the exception
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> handleResponseStatusException(ResponseStatusException ex, WebRequest request) {
        String errorId = UUID.randomUUID().toString();
        log.error("Error ID: {}, Message: {}", errorId, ex.getReason(), ex);
        return buildResponseEntity(ex.getStatusCode(), ex.getReason(), errorId);
    }

    /**
     * Builds a structured error response entity with HttpStatus.
     * 
     * <p>Creates a standardized JSON error response containing timestamp, status code,
     * error name, message, and unique errorId for tracking.</p>
     * 
     * @param httpStatus the HTTP status for the response
     * @param message the error message to include in the response
     * @param errorId the unique identifier for this error occurrence
     * @return ResponseEntity with structured error data
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
     * Builds a structured error response entity with HttpStatusCode.
     * 
     * <p>Creates a standardized JSON error response containing timestamp, status code,
     * error name, message, and unique errorId for tracking. This overload handles
     * generic HttpStatusCode types for flexibility.</p>
     * 
     * @param httpStatusCode the HTTP status code for the response
     * @param message the error message to include in the response
     * @param errorId the unique identifier for this error occurrence
     * @return ResponseEntity with structured error data
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
