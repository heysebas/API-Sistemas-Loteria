package org.konex.sistemaloteria.excepciones;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones para toda la aplicación.
 *
 * <p>
 * Esta clase centraliza el control de errores y define las respuestas
 * estandarizadas que el backend devuelve ante distintos tipos de excepciones.
 * </p>
 *
 * <p>
 * Anotada con {@link RestControllerAdvice}, actúa como un interceptor global
 * para cualquier excepción lanzada dentro de los controladores REST.
 * </p>
 *
 * <p>
 * Devuelve respuestas JSON con un formato uniforme:
 * <pre>
 * {
 *   "timestamp": "2025-11-08T22:45:12",
 *   "status": 400,
 *   "error": "Error de validación",
 *   "messages": {
 *       "nombre": "El nombre es obligatorio"
 *   }
 * }
 * </pre>
 * </p>
 *
 * <p>
 * Esta práctica cumple con las recomendaciones de manejo de errores definidas
 * en la <b>Prueba Técnica – Sistema de Ventas de Lotería</b> de Konex Innovation:contentReference[oaicite:0]{index=0}.
 * </p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja las excepciones generadas por validaciones fallidas en DTOs
     * (por ejemplo, campos con {@link jakarta.validation.constraints.NotNull} o {@link jakarta.validation.constraints.NotBlank}).
     *
     * <p>
     * Se activa cuando un controlador recibe datos inválidos
     * y arroja {@link MethodArgumentNotValidException}.
     * </p>
     *
     * @param ex excepción de validación capturada por Spring.
     * @return una respuesta con detalles del error y mensajes específicos por campo.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Error de validación");

        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err ->
                fieldErrors.put(err.getField(), err.getDefaultMessage())
        );
        body.put("messages", fieldErrors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    /**
     * Maneja los errores de tipo {@link IllegalArgumentException},
     * normalmente lanzados cuando se envían parámetros inválidos o
     * no se encuentra una entidad esperada (por ejemplo, cliente o billete inexistente).
     *
     * @param ex excepción capturada.
     * @return respuesta con estado HTTP 400 (Bad Request) y mensaje descriptivo.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Solicitud incorrecta");
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    /**
     * Maneja los errores de tipo {@link IllegalStateException},
     * usados cuando una operación es inconsistente con el estado actual
     * del sistema (por ejemplo, intentar vender un billete ya vendido).
     *
     * @param ex excepción capturada.
     * @return respuesta con estado HTTP 409 (Conflict).
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalState(IllegalStateException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", HttpStatus.CONFLICT.value());
        body.put("error", "Conflicto en la operación");
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    /**
     * Manejador genérico para cualquier otra excepción no controlada
     * de manera específica.
     *
     * <p>
     * Devuelve una respuesta estándar con estado HTTP 500 (Internal Server Error),
     * preservando el mensaje original de la excepción.
     * </p>
     *
     * @param ex excepción inesperada.
     * @return respuesta genérica con detalles básicos del error.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(Exception ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Error interno del servidor");
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
