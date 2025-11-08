package org.konex.sistemaloteria.cliente.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.konex.sistemaloteria.cliente.dto.ClienteDto;
import org.konex.sistemaloteria.cliente.service.ClienteService;
import org.konex.sistemaloteria.billete.dto.BilleteDto;
import org.konex.sistemaloteria.billete.repository.BilleteRepository;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService service;
    private final BilleteRepository billeteRepo;

    @PostMapping
    public ResponseEntity<ClienteDto> registrar(@Valid @RequestBody ClienteDto dto) {
        return ResponseEntity.ok(service.registrar(dto));
    }

    @GetMapping
    public ResponseEntity<List<ClienteDto>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDto> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteDto> actualizar(@PathVariable Long id, @Valid @RequestBody ClienteDto dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // Billetes vendidos a un cliente
    @GetMapping("/{clienteId}/billetes")
    public ResponseEntity<List<BilleteDto>> billetesDeCliente(@PathVariable Long clienteId) {
        var out = billeteRepo.findByClienteId(clienteId).stream().map(b -> {
            BilleteDto dto = new BilleteDto();
            dto.setId(b.getId());
            dto.setNumero(b.getNumero());
            dto.setPrecio(b.getPrecio());
            dto.setEstado(b.getEstado());
            dto.setSorteoId(b.getSorteo().getId());
            return dto;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(out);
    }
}
