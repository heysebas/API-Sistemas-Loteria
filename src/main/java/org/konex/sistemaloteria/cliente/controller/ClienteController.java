package org.konex.sistemaloteria.cliente.controller;

import lombok.RequiredArgsConstructor;
import org.konex.sistemaloteria.cliente.dto.HistorialClienteDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.konex.sistemaloteria.cliente.dto.ClienteDto;
import org.konex.sistemaloteria.cliente.service.ClienteService;
import org.konex.sistemaloteria.billete.dto.BilleteDto;
import org.konex.sistemaloteria.billete.repository.BilleteRepository;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador REST que gestiona las operaciones relacionadas con clientes.
 *
 * <p>
 * Expone los endpoints CRUD básicos y consultas adicionales:
 * <ul>
 *   <li>Registro, actualización y eliminación de clientes.</li>
 *   <li>Listado general y obtención por ID.</li>
 *   <li>Consulta de billetes comprados por cliente.</li>
 *   <li>Historial completo de compras por correo.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Todos los endpoints devuelven respuestas en formato JSON y utilizan
 * validaciones automáticas con {@link Valid} en las solicitudes de entrada.
 * </p>
 *
 * <p>
 * Errores comunes (manejados por {@code GlobalExceptionHandler}):
 * <ul>
 *   <li><b>400 BAD REQUEST</b>: datos inválidos o duplicados.</li>
 *   <li><b>404 NOT FOUND</b>: cliente no encontrado.</li>
 * </ul>
 * </p>
 *
 * <h4>Ruta base:</h4>
 * <pre>/api/clientes</pre>
 *
 * <p>
 * Forma parte del módulo de clientes del
 * <b>Sistema de Ventas de Lotería</b> — Prueba Técnica Konex Innovation:contentReference[oaicite:0]{index=0}.
 * </p>
 */
@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService service;
    private final BilleteRepository billeteRepo;

    // ======================================================
    // 🔹 CRUD DE CLIENTES
    // ======================================================

    /**
     * Registra un nuevo cliente.
     *
     * <h4>Ejemplo de solicitud (POST /api/clientes)</h4>
     * <pre>
     * {
     *   "nombre": "Juan Pérez",
     *   "correo": "juan@example.com"
     * }
     * </pre>
     *
     * @param dto datos del cliente a registrar.
     * @return cliente creado en formato {@link ClienteDto}.
     */
    @PostMapping
    public ResponseEntity<ClienteDto> registrar(@Valid @RequestBody ClienteDto dto) {
        return ResponseEntity.ok(service.registrar(dto));
    }

    /**
     * Lista todos los clientes registrados.
     *
     * <h4>Ejemplo:</h4>
     * <pre>GET /api/clientes</pre>
     *
     * @return lista de clientes en formato {@link ClienteDto}.
     */
    @GetMapping
    public ResponseEntity<List<ClienteDto>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    /**
     * Obtiene la información de un cliente específico por su ID.
     *
     * <h4>Ejemplo:</h4>
     * <pre>GET /api/clientes/1</pre>
     *
     * @param id identificador del cliente.
     * @return cliente encontrado como {@link ClienteDto}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClienteDto> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    /**
     * Actualiza los datos de un cliente.
     *
     * <h4>Ejemplo:</h4>
     * <pre>PUT /api/clientes/2</pre>
     * <pre>
     * {
     *   "nombre": "Ana Gómez",
     *   "correo": "ana.gomez@example.com"
     * }
     * </pre>
     *
     * @param id  identificador del cliente.
     * @param dto datos nuevos.
     * @return cliente actualizado como {@link ClienteDto}.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ClienteDto> actualizar(@PathVariable Long id, @Valid @RequestBody ClienteDto dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    /**
     * Elimina un cliente por su identificador.
     *
     * <h4>Ejemplo:</h4>
     * <pre>DELETE /api/clientes/3</pre>
     *
     * @param id identificador del cliente a eliminar.
     * @return respuesta sin contenido (204 No Content).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // ======================================================
    // 🔹 CONSULTAS DE BILLETES E HISTORIAL
    // ======================================================

    /**
     * Obtiene los billetes comprados por un cliente específico.
     *
     * <h4>Ejemplo:</h4>
     * <pre>GET /api/clientes/5/billetes</pre>
     *
     * @param clienteId identificador del cliente.
     * @return lista de billetes del cliente en formato {@link BilleteDto}.
     */
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
     * Obtiene el historial de billetes vendidos de un cliente a partir de su correo electrónico.
     *
     * <h4>Ejemplo:</h4>
     * <pre>GET /api/clientes/historial?correo=juan@example.com</pre>
     *
     * @param correo correo del cliente a consultar.
     * @return historial completo del cliente como {@link HistorialClienteDto}.
     */
    @GetMapping("/historial")
    public ResponseEntity<HistorialClienteDto> historial(@RequestParam String correo) {
        return ResponseEntity.ok(service.obtenerHistorialPorCorreo(correo));
    }

    /**
     * Variante del endpoint anterior que permite usar el correo directamente en la URL.
     *
     * <h4>Ejemplo:</h4>
     * <pre>GET /api/clientes/correo/ana.gomez@example.com</pre>
     *
     * <p>
     * Se incluye el patrón <code>:.+</code> para permitir puntos en el correo electrónico.
     * </p>
     *
     * @param correo correo electrónico del cliente.
     * @return historial de billetes del cliente.
     */
    @GetMapping("/correo/{correo:.+}")
    public ResponseEntity<HistorialClienteDto> historialByPath(@PathVariable("correo") String correo) {
        return ResponseEntity.ok(service.obtenerHistorialPorCorreo(correo));
    }
}
