package org.konex.sistemaloteria.excepciones;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Manejador global de excepciones para toda la aplicación.
 *
 * Respuesta consistente:
 * {
 *   "timestamp": "2025-11-09T07:30:00.123",
 *   "status": 400,
 *   "error": "Error de validación",
 *   "message": "Hay errores de validación",
 *   "path": "/api/billetes",
 *   "method": "POST",
 *   "errors": { "numero": "debe tener entre 1 y 6 dígitos" }
 * }
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /* ==================== Helpers ==================== */

    private Map<String, Object> baseBody(HttpStatus status,
                                         String error,
                                         String message,
                                         HttpServletRequest req) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", message);
        if (req != null) {
            body.put("path", req.getRequestURI());
            body.put("method", req.getMethod());
        }
        return body;
    }

    private ResponseEntity<Map<String, Object>> build(HttpStatus status,
                                                      String error,
                                                      String message,
                                                      HttpServletRequest req,
                                                      Map<String, ?> errors) {
        Map<String, Object> body = baseBody(status, error, message, req);
        if (errors != null && !errors.isEmpty()) {
            body.put("errors", errors);
        }
        return ResponseEntity.status(status).body(body);
    }

    private ResponseEntity<Map<String, Object>> build(HttpStatus status,
                                                      String error,
                                                      String message,
                                                      HttpServletRequest req) {
        return build(status, error, message, req, null);
    }

    /* ============ Validación de DTOs (@RequestBody) ============ */

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest req) {

        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(err -> fieldErrors.put(err.getField(), err.getDefaultMessage()));
        ex.getBindingResult().getGlobalErrors()
                .forEach(err -> fieldErrors.put(err.getObjectName(), err.getDefaultMessage()));

        return build(HttpStatus.BAD_REQUEST,
                "Error de validación",
                "Hay errores de validación",
                req,
                fieldErrors);
    }

    /* ==== Validación en @RequestParam / @PathVariable ==== */

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolation(
            ConstraintViolationException ex,
            HttpServletRequest req) {

        Map<String, String> errors = new HashMap<>();
        for (ConstraintViolation<?> v : ex.getConstraintViolations()) {
            errors.put(v.getPropertyPath().toString(), v.getMessage());
        }
        return build(HttpStatus.BAD_REQUEST,
                "Error de validación (parámetros)",
                "Parámetros inválidos",
                req,
                errors);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest req) {

        Map<String, String> errors = new HashMap<>();
        String name = ex.getName(); // p.ej. "sorteoId"
        String required = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "tipo esperado";
        errors.put(name, "Tipo inválido. Se esperaba " + required);
        return build(HttpStatus.BAD_REQUEST,
                "Tipo de parámetro inválido",
                "Tipo de parámetro inválido",
                req,
                errors);
    }

    /* ============ Lectura de JSON / Enums / Formatos ============ */

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleNotReadable(
            HttpMessageNotReadableException ex,
            HttpServletRequest req) {

        return build(HttpStatus.BAD_REQUEST,
                "Cuerpo de solicitud inválido",
                "JSON mal formado o valor incompatible (verifica números y enums).",
                req);
    }

    /* ============ Reglas de negocio / Dominio ============ */

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(
            IllegalArgumentException ex,
            HttpServletRequest req) {

        return build(HttpStatus.BAD_REQUEST,
                "Solicitud incorrecta",
                ex.getMessage(),
                req);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalState(
            IllegalStateException ex,
            HttpServletRequest req) {

        return build(HttpStatus.CONFLICT,
                "Conflicto en la operación",
                ex.getMessage(),
                req);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(
            NoSuchElementException ex,
            HttpServletRequest req) {

        return build(HttpStatus.NOT_FOUND,
                "Recurso no encontrado",
                ex.getMessage(),
                req);
    }

    /* ============ Persistencia ============ */

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrity(
            DataIntegrityViolationException ex,
            HttpServletRequest req) {

        String cause = ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage();
        return build(HttpStatus.CONFLICT,
                "Restricción de datos",
                "Violación de integridad de datos (duplicados o referencias inválidas). " + cause,
                req);
    }

    /* ============ HTTP ============ */

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, Object>> handleMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            HttpServletRequest req) {

        return build(HttpStatus.METHOD_NOT_ALLOWED,
                "Método HTTP no soportado",
                "Método no soportado: " + ex.getMethod(),
                req);
    }

    /* ============ Fallback genérico ============ */

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(
            Exception ex,
            HttpServletRequest req) {

        return build(HttpStatus.INTERNAL_SERVER_ERROR,
                "Error interno del servidor",
                ex.getMessage(),
                req);
    }
}
