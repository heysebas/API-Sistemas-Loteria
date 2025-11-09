package org.konex.sistemaloteria.sorteo.controller;

import lombok.RequiredArgsConstructor;
import org.konex.sistemaloteria.billete.model.Billete;
import org.konex.sistemaloteria.sorteo.dto.SorteoDto;
import org.konex.sistemaloteria.sorteo.service.SorteoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * Controlador REST encargado de gestionar los sorteos y sus billetes.
 *
 * <p>
 * Expone los endpoints bajo la ruta base <b>/api/sorteos</b> y
 * delega toda la lógica de negocio al servicio {@link SorteoService}.
 * </p>
 *
 * <p>
 * Este controlador cumple con los requisitos del módulo "Sorteos"
 * descrito en la <b>Prueba Técnica – Sistema de Ventas de Lotería</b> de Konex Innovation:contentReference[oaicite:1]{index=1}.
 * </p>
 *
 * <h3>Endpoints principales:</h3>
 * <ul>
 *   <li><b>POST /api/sorteos</b> — Crear un nuevo sorteo.</li>
 *   <li><b>GET /api/sorteos</b> — Listar todos los sorteos registrados.</li>
 *   <li><b>POST /api/sorteos/{id}/billetes</b> — Generar billetes para un sorteo existente.</li>
 *   <li><b>GET /api/sorteos/{id}/billetes</b> — Consultar los billetes de un sorteo.</li>
 * </ul>
 *
 * <p>
 * Todas las respuestas se devuelven como {@link ResponseEntity} con el estado HTTP adecuado.
 * </p>
 */
@RestController
@RequestMapping("/api/sorteos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SorteoController {

    /** Servicio que contiene la lógica de negocio de los sorteos. */
    private final SorteoService service;

    /**
     * Crea un nuevo sorteo.
     *
     * <p>
     * Valida que los campos requeridos estén presentes en el cuerpo de la solicitud,
     * usando anotaciones de {@link jakarta.validation.Valid}.
     * </p>
     *
     * <h4>Ejemplo de solicitud JSON:</h4>
     * <pre>
     * {
     *   "nombre": "Sorteo de Navidad",
     *   "fechaSorteo": "2025-12-24"
     * }
     * </pre>
     *
     * <h4>Ejemplo de respuesta (HTTP 201):</h4>
     * <pre>
     * {
     *   "id": 1,
     *   "nombre": "Sorteo de Navidad",
     *   "fechaSorteo": "2025-12-24"
     * }
     * </pre>
     *
     * @param dto objeto {@link SorteoDto} con el nombre y fecha del sorteo.
     * @return el sorteo creado, con código de estado {@link HttpStatus#CREATED}.
     */
    @PostMapping
    public ResponseEntity<SorteoDto> crear(@Valid @RequestBody SorteoDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    /**
     * Obtiene el listado de todos los sorteos registrados.
     *
     * <h4>Ejemplo de respuesta:</h4>
     * <pre>
     * [
     *   { "id": 1, "nombre": "Sorteo de Navidad", "fechaSorteo": "2025-12-24" },
     *   { "id": 2, "nombre": "Sorteo de Año Nuevo", "fechaSorteo": "2025-12-31" }
     * ]
     * </pre>
     *
     * @return una lista de {@link SorteoDto}.
     */
    @GetMapping
    public ResponseEntity<List<SorteoDto>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    /**
     * Genera una cantidad específica de billetes para un sorteo existente.
     *
     * <p>
     * Cada billete se numera secuencialmente (0001, 0002, …), se asocia al sorteo
     * y se marca como {@code DISPONIBLE}.
     * </p>
     *
     * <h4>Ejemplo de petición:</h4>
     * <pre>
     * POST /api/sorteos/1/billetes?cantidad=10&precio=10000
     * </pre>
     *
     * <h4>Ejemplo de respuesta:</h4>
     * <pre>
     * [
     *   { "id": 1, "numero": "0001", "precio": 10000, "estado": "DISPONIBLE" },
     *   { "id": 2, "numero": "0002", "precio": 10000, "estado": "DISPONIBLE" }
     * ]
     * </pre>
     *
     * @param id identificador del sorteo.
     * @param cantidad número de billetes a generar.
     * @param precio precio unitario de cada billete.
     * @return lista de billetes generados para el sorteo.
     */
    @PostMapping("/{id}/billetes")
    public ResponseEntity<List<Billete>> generarBilletes(
            @PathVariable Long id,
            @RequestParam int cantidad,
            @RequestParam double precio
    ) {
        List<Billete> billetes = service.generarBilletes(id, cantidad, precio);
        return ResponseEntity.ok(billetes);
    }

    /**
     * Lista todos los billetes asociados a un sorteo.
     *
     * <p>
     * Permite visualizar tanto billetes disponibles como vendidos,
     * dependiendo del estado actual de cada uno.
     * </p>
     *
     * <h4>Ejemplo de petición:</h4>
     * <pre>
     * GET /api/sorteos/1/billetes
     * </pre>
     *
     * @param id identificador del sorteo.
     * @return lista de billetes pertenecientes al sorteo.
     */
    @GetMapping("/{id}/billetes")
    public ResponseEntity<List<Billete>> listarBilletesPorSorteo(@PathVariable Long id) {
        List<Billete> billetes = service.listarBilletesPorSorteo(id);
        return ResponseEntity.ok(billetes);
    }
}
