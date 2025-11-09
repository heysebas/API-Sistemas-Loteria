package org.konex.sistemaloteria.venta.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.konex.sistemaloteria.venta.dto.VentaRequestDto;
import org.konex.sistemaloteria.venta.dto.VentaResponseDto;
import org.konex.sistemaloteria.venta.service.VentaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para gestionar ventas de billetes.
 * Ruta base: /api/ventas
 */
@RestController
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
public class VentaController {

    private final VentaService service;

    /**
     * Registra una nueva venta de billete.
     * Devuelve 201 Created y JSON.
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VentaResponseDto> vender(@Valid @RequestBody VentaRequestDto request) {
        VentaResponseDto response = service.vender(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
