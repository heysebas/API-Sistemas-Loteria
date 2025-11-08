package org.konex.sistemaloteria.billete.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.konex.sistemaloteria.billete.dto.BilleteDto;
import org.konex.sistemaloteria.billete.service.BilleteService;

import java.util.List;

@RestController
@RequestMapping("/api/billetes")
@RequiredArgsConstructor
public class BilleteController {

    private final BilleteService service;

    @PostMapping
    public ResponseEntity<BilleteDto> crear(@RequestBody BilleteDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crearBillete(dto));
    }

    @GetMapping("/sorteo/{sorteoId}")
    public ResponseEntity<List<BilleteDto>> listarPorSorteo(@PathVariable Long sorteoId) {
        return ResponseEntity.ok(service.listarPorSorteo(sorteoId));
    }
}
