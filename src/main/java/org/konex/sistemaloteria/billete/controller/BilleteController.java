package org.konex.sistemaloteria.billete.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.konex.sistemaloteria.billete.dto.BilleteDto;
import org.konex.sistemaloteria.billete.service.BilleteService;

import java.util.List;

/**
 * Controlador REST para la gestión de billetes.
 *
 * <p>
 * Expone los endpoints relacionados con la creación y consulta de billetes
 * dentro del <b>Sistema de Ventas de Lotería</b>.
 * </p>
 *
 * <p>
 * Todos los endpoints devuelven respuestas en formato JSON
 * y siguen las convenciones REST, utilizando {@link ResponseEntity}
 * para definir el estado HTTP de la respuesta.
 * </p>
 *
 * <h3>Ruta base:</h3>
 * <pre>/api/billetes</pre>
 *
 * <h3>Endpoints disponibles:</h3>
 * <ul>
 *   <li><b>POST /api/billetes</b> — Crear un nuevo billete.</li>
 *   <li><b>GET /api/billetes/sorteo/{sorteoId}</b> — Listar billetes de un sorteo.</li>
 * </ul>
 *
 * <p>
 * Forma parte del módulo <b>Billete</b> del backend desarrollado para la
 * <b>Prueba Técnica – Konex Innovation</b>:contentReference[oaicite:1]{index=1}.
 * </p>
 */
@RestController
@RequestMapping("/api/billetes")
@RequiredArgsConstructor
public class BilleteController {

    /** Servicio que contiene la lógica de negocio para billetes. */
    private final BilleteService service;

    /**
     * Crea un nuevo billete asociado a un sorteo existente.
     *
     * <h4>Ejemplo de solicitud (POST /api/billetes)</h4>
     * <pre>
     * {
     *   "numero": "0009",
     *   "precio": 10000,
     *   "estado": "DISPONIBLE",
     *   "sorteoId": 2
     * }
     * </pre>
     *
     * <h4>Respuesta (HTTP 201 Created)</h4>
     * <pre>
     * {
     *   "id": 45,
     *   "numero": "0009",
     *   "precio": 10000,
     *   "estado": "DISPONIBLE",
     *   "sorteoId": 2
     * }
     * </pre>
     *
     * <p>
     * En caso de que el sorteo no exista, se devuelve una excepción controlada
     * con código <b>400 Bad Request</b> manejada por {@code GlobalExceptionHandler}.
     * </p>
     *
     * @param dto datos del billete a registrar.
     * @return el billete creado con su ID y estado actualizados.
     */
    @PostMapping
    public ResponseEntity<BilleteDto> crear(@RequestBody BilleteDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crearBillete(dto));
    }

    /**
     * Lista todos los billetes pertenecientes a un sorteo específico.
     *
     * <h4>Ejemplo de solicitud:</h4>
     * <pre>GET /api/billetes/sorteo/3</pre>
     *
     * <h4>Respuesta (HTTP 200 OK):</h4>
     * <pre>
     * [
     *   {
     *     "id": 12,
     *     "numero": "0005",
     *     "precio": 8000,
     *     "estado": "VENDIDO",
     *     "sorteoId": 3
     *   },
     *   {
     *     "id": 13,
     *     "numero": "0006",
     *     "precio": 8000,
     *     "estado": "DISPONIBLE",
     *     "sorteoId": 3
     *   }
     * ]
     * </pre>
     *
     * @param sorteoId identificador del sorteo cuyos billetes se quieren listar.
     * @return lista de billetes en formato {@link BilleteDto}.
     */
    @GetMapping("/sorteo/{sorteoId}")
    public ResponseEntity<List<BilleteDto>> listarPorSorteo(@PathVariable Long sorteoId) {
        return ResponseEntity.ok(service.listarPorSorteo(sorteoId));
    }
}
