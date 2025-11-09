package org.konex.sistemaloteria.configuracion;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

/**
 * Controlador REST utilizado para verificar el estado general de la API.
 *
 * <p>
 * Expone un único endpoint (<b>/api/status</b>) que permite comprobar
 * si el servicio backend se encuentra disponible y operativo.
 * </p>
 *
 * <p>
 * Este tipo de endpoint es útil para:
 * <ul>
 *   <li>Pruebas de conexión desde el frontend (Angular).</li>
 *   <li>Monitoreo automatizado o pruebas de despliegue.</li>
 *   <li>Validar que el servidor y el contexto de Spring Boot estén activos.</li>
 * </ul>
 * </p>
 *
 * <h4>Ejemplo de solicitud:</h4>
 * <pre>
 * GET /api/status
 * </pre>
 *
 * <h4>Ejemplo de respuesta JSON (HTTP 200):</h4>
 * <pre>
 * {
 *   "status": "OK",
 *   "message": "API SistemaLoteria funcionando correctamente",
 *   "timestamp": "2025-11-08T22:45:30.812"
 * }
 * </pre>
 *
 * <p>
 * Forma parte de la configuración base del proyecto exigida en la
 * <b>Prueba Técnica – Sistema de Ventas de Lotería</b> de Konex Innovation:contentReference[oaicite:0]{index=0}.
 * </p>
 */
@RestController
public class StatusController {

    /**
     * Devuelve información básica sobre el estado actual de la API.
     *
     * @return un mapa con el estado, un mensaje descriptivo y la marca de tiempo actual.
     */
    @GetMapping("/api/status")
    public Map<String, Object> status() {
        return Map.of(
                "status", "OK",
                "message", "API SistemaLoteria funcionando correctamente",
                "timestamp", java.time.LocalDateTime.now().toString()
        );
    }
}
