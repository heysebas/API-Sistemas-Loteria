package org.konex.sistemaloteria.cliente.controller;

import lombok.RequiredArgsConstructor;
import org.konex.sistemaloteria.billete.dto.BilleteDto;
import org.konex.sistemaloteria.billete.repository.BilleteRepository;
import org.konex.sistemaloteria.cliente.dto.ClienteDto;
import org.konex.sistemaloteria.cliente.dto.HistorialClienteDto;
import org.konex.sistemaloteria.cliente.service.ClienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador REST que gestiona las operaciones relacionadas con clientes.
 *
 * Endpoints principales:
 * - CRUD de clientes.
 * - Billetes por cliente.
 * - Historial de compras por correo (valida formato de correo).
 */
@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@Validated // Habilita la validación en parámetros de request (Query/Path)
public class ClienteController {

    private final ClienteService service;
    private final BilleteRepository billeteRepo;

    // ======================================================
    // 🔹 CRUD DE CLIENTES
    // ======================================================

    /**
     * Crea un nuevo cliente.
     * Ejemplo (POST /api/clientes):
     * {
     *   "nombre": "Juan Pérez",
     *   "correo": "juan@example.com"
     * }
     */
    @PostMapping
    public ResponseEntity<ClienteDto> crear(@Valid @RequestBody ClienteDto dto) {
        return ResponseEntity.status(201).body(service.crearCliente(dto));
    }

    /** Lista todos los clientes registrados. */
    @GetMapping
    public ResponseEntity<List<ClienteDto>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    /** Obtiene los datos de un cliente por su ID. */
    @GetMapping("/{id}")
    public ResponseEntity<ClienteDto> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    /** Actualiza los datos de un cliente existente. */
    @PutMapping("/{id}")
    public ResponseEntity<ClienteDto> actualizar(@PathVariable Long id, @Valid @RequestBody ClienteDto dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    /** Elimina un cliente por su identificador. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // ======================================================
    // 🔹 CONSULTAS DE BILLETES E HISTORIAL
    // ======================================================

    /** Obtiene los billetes asociados a un cliente. */
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

    /**
     * Historial completo de billetes de un cliente por correo.
     * Valida que el correo no sea vacío y tenga formato válido.
     * Ejemplo: GET /api/clientes/historial?correo=juan@example.com
     */
    @GetMapping("/historial")
    public ResponseEntity<HistorialClienteDto> historial(
            @RequestParam
            @NotBlank(message = "El correo es obligatorio")
            @Email(message = "El correo no tiene un formato válido")
            String correo) {

        return ResponseEntity.ok(service.historialPorCorreo(correo));
    }

    /**
     * Variante por path. También valida formato de correo.
     * Ejemplo: GET /api/clientes/correo/ana.gomez@example.com
     */
    @GetMapping("/correo/{correo:.+}")
    public ResponseEntity<HistorialClienteDto> historialByPath(
            @PathVariable("correo")
            @Email(message = "El correo no tiene un formato válido")
            String correo) {

        return ResponseEntity.ok(service.historialPorCorreo(correo));
    }
}
