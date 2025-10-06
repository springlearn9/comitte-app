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

@ControllerAdvice
@Slf4j
public class ApiExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception ex, WebRequest request) {
        String errorId = UUID.randomUUID().toString();
        log.error("Error ID: {}, Message: {}", errorId, ex.getMessage(), ex);
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), errorId);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> handleResponseStatusException(ResponseStatusException ex, WebRequest request) {
        String errorId = UUID.randomUUID().toString();
        log.error("Error ID: {}, Message: {}", errorId, ex.getReason(), ex);
        return buildResponseEntity(ex.getStatusCode(), ex.getReason(), errorId);
    }

    private ResponseEntity<Map<String, Object>> buildResponseEntity(HttpStatus httpStatus, String message, String errorId) {
        return ResponseEntity.status(httpStatus).body(Map.of(
                "timestamp", LocalDateTime.now(),
                "status", httpStatus.value(),
                "error", httpStatus.getReasonPhrase(),
                "message", message,
                "errorId", errorId
        ));
    }

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
