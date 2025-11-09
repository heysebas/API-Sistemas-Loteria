package org.konex.sistemaloteria.venta.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.konex.sistemaloteria.venta.dto.VentaRequestDto;
import org.konex.sistemaloteria.venta.dto.VentaResponseDto;
import org.konex.sistemaloteria.venta.service.VentaService;

import jakarta.validation.Valid;

/**
 * Controlador REST encargado de gestionar las operaciones de ventas.
 *
 * <p>
 * Expone los endpoints bajo la ruta base <b>/api/ventas</b>
 * y delega la lógica de negocio al {@link VentaService}.
 * </p>
 *
 * <p>
 * Actualmente, el controlador ofrece una única operación:
 * registrar una nueva venta de billete mediante una petición POST.
 * </p>
 *
 * <p>
 * Ejemplo de solicitud (JSON):
 * <pre>
 * {
 *   "billeteId": 10,
 *   "clienteId": 3
 * }
 * </pre>
 * </p>
 *
 * Ejemplo de respuesta (JSON):
 * <pre>
 * {
 *   "ventaId": 1,
 *   "billeteId": 10,
 *   "numeroBillete": "0001",
 *   "clienteId": 3,
 *   "nombreCliente": "Juan Pérez",
 *   "fechaVenta": "2025-11-08T19:34:21",
 *   "precio": 10000
 * }
 * </pre>
 */
@RestController
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
public class VentaController {

    /** Servicio de ventas que contiene la lógica de registro de transacciones. */
    private final VentaService service;

    /**
     * Registra una nueva venta de billete.
     *
     * <p>
     * Se valida que el cuerpo de la solicitud contenga los campos requeridos
     * mediante anotaciones de {@link jakarta.validation.Valid}.
     * </p>
     *
     * @param request datos del billete y cliente para la venta.
     * @return {@link ResponseEntity} con un {@link VentaResponseDto} si la venta se realiza correctamente.
     */
    @PostMapping
    public ResponseEntity<VentaResponseDto> vender(@Valid @RequestBody VentaRequestDto request) {
        return ResponseEntity.ok(service.vender(request));
    }
}
