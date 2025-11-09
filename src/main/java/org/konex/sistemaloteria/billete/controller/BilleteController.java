package org.konex.sistemaloteria.billete.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.konex.sistemaloteria.billete.dto.BilleteDto;
import org.konex.sistemaloteria.billete.service.BilleteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de billetes.
 *
 * <p>Expone los endpoints de creación y consulta de billetes dentro del
 * <b>Sistema de Ventas de Lotería</b>.</p>
 *
 * <p>Todos los endpoints devuelven JSON y usan {@link ResponseEntity}
 * para definir el estado HTTP.</p>
 *
 * <h3>Ruta base:</h3>
 * <pre>/api/billetes</pre>
 *
 * <h3>Endpoints:</h3>
 * <ul>
 *   <li><b>POST /api/billetes</b> — Crear un billete.</li>
 *   <li><b>GET  /api/billetes/sorteo/{sorteoId}</b> — Listar billetes de un sorteo.</li>
 * </ul>
 *
 * <p>Validaciones esperadas (definidas en {@link BilleteDto}):
 * <ul>
 *   <li><code>numero</code>: 1 a 6 dígitos numéricos.</li>
 *   <li><code>precio</code>: &gt; 0.</li>
 *   <li><code>sorteoId</code>: no nulo.</li>
 * </ul>
 * </p>
 */
@RestController
@RequestMapping("/api/billetes")
@RequiredArgsConstructor
public class BilleteController {

    /** Servicio con la lógica de billetes. */
    private final BilleteService service;

    /**
     * Crea un nuevo billete asociado a un sorteo existente.
     *
     * <h4>Ejemplo (POST /api/billetes)</h4>
     * <pre>
     * {
     *   "numero": "0009",
     *   "precio": 10000,
     *   "estado": "DISPONIBLE",
     *   "sorteoId": 2
     * }
     * </pre>
     *
     * @param dto datos del billete (validado).
     * @return billete creado (HTTP 201).
     */
    @PostMapping
    public ResponseEntity<BilleteDto> crear(@Valid @RequestBody BilleteDto dto) {
        BilleteDto creado = service.crearBillete(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    /**
     * Lista los billetes de un sorteo específico.
     *
     * <h4>Ejemplo</h4>
     * <pre>GET /api/billetes/sorteo/3</pre>
     *
     * @param sorteoId ID del sorteo.
     * @return lista de billetes (HTTP 200).
     */
    @GetMapping("/sorteo/{sorteoId}")
    public ResponseEntity<List<BilleteDto>> listarPorSorteo(@PathVariable Long sorteoId) {
        return ResponseEntity.ok(service.listarPorSorteo(sorteoId));
    }
}
