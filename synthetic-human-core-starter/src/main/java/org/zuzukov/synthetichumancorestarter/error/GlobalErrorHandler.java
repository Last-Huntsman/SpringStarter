package org.zuzukov.synthetichumancorestarter.error;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.*;

@RestControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex,
                                                                         HttpServletRequest request) {
        List<Map<String, String>> fieldErrors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            Map<String, String> fieldError = new HashMap<>();
            fieldError.put("field", error.getField());
            fieldError.put("message", error.getDefaultMessage());
            fieldErrors.add(fieldError);
        }

        return buildErrorResponse(HttpStatus.BAD_REQUEST, fieldErrors, request.getRequestURI());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleJsonParseError(HttpMessageNotReadableException ex,
                                                                    HttpServletRequest request) {
        List<Map<String, String>> fieldErrors = new ArrayList<>();

        String message = Optional.ofNullable(ex.getMostSpecificCause())
                .map(Throwable::getMessage)
                .orElse("Ошибка разбора JSON");

        String fieldName = extractFieldNameFromEnumError(message);

        Map<String, String> error = new HashMap<>();
        error.put("field", fieldName);
        error.put("message", message);
        fieldErrors.add(error);

        return buildErrorResponse(HttpStatus.BAD_REQUEST, fieldErrors, request.getRequestURI());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolation(ConstraintViolationException ex,
                                                                         HttpServletRequest request) {
        List<Map<String, String>> fieldErrors = new ArrayList<>();
        ex.getConstraintViolations().forEach(violation -> {
            Map<String, String> fieldError = new HashMap<>();
            fieldError.put("field", violation.getPropertyPath().toString());
            fieldError.put("message", violation.getMessage());
            fieldErrors.add(fieldError);
        });

        return buildErrorResponse(HttpStatus.BAD_REQUEST, fieldErrors, request.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex,
                                                                      HttpServletRequest request) {
        Map<String, String> error = new HashMap<>();
        error.put("field", "unknown");
        error.put("message", ex.getMessage());

        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                Collections.singletonList(error),
                request.getRequestURI());
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status,
                                                                   List<Map<String, String>> errors,
                                                                   String path) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("errors", errors);
        body.put("path", path);
        return ResponseEntity.status(status).body(body);
    }

    private String extractFieldNameFromEnumError(String message) {
        if (message.contains("from String")) {
            int start = message.indexOf("`");
            int end = message.indexOf("`", start + 1);
            if (start != -1 && end != -1) {
                String enumClass = message.substring(start + 1, end);
                return enumClass.substring(enumClass.lastIndexOf('.') + 1).toLowerCase(); // Priority → priority
            }
        }
        return "unknown";
    }
}
