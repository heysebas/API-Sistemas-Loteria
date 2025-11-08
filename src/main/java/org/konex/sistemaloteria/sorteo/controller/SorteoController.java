package org.konex.sistemaloteria.sorteo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.konex.sistemaloteria.sorteo.service.SorteoService;
import org.konex.sistemaloteria.sorteo.dto.SorteoDto;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/sorteos")
@RequiredArgsConstructor
public class SorteoController {

    private final SorteoService service;

    @PostMapping
    public ResponseEntity<SorteoDto> crear(@Valid @RequestBody SorteoDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @GetMapping
    public ResponseEntity<List<SorteoDto>> listar() {
        return ResponseEntity.ok(service.listar());
    }
}
