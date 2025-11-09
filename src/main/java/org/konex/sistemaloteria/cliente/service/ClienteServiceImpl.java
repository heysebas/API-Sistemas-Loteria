package org.konex.sistemaloteria.cliente.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import org.konex.sistemaloteria.cliente.dto.ClienteDto;
import org.konex.sistemaloteria.cliente.dto.HistorialClienteDto;
import org.konex.sistemaloteria.cliente.model.Cliente;
import org.konex.sistemaloteria.cliente.repository.ClienteRepository;
import org.konex.sistemaloteria.venta.repository.VentaRepository;
import org.konex.sistemaloteria.billete.model.Billete;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio de dominio para la gestión de clientes.
 *
 * <p>Responsabilidades principales:</p>
 * <ul>
 *   <li>Registrar, listar, obtener, actualizar y eliminar clientes.</li>
 *   <li>Consultar el historial de compras (billetes vendidos) de un cliente por correo.</li>
 * </ul>
 *
 * <p>Convenciones de error:</p>
 * <ul>
 *   <li>{@link IllegalArgumentException} para solicitudes inválidas (datos faltantes o duplicados).</li>
 *   <li>{@link ResponseStatusException} con 404 cuando el cliente no existe al consultar historial.</li>
 * </ul>
 *
 * <p>Nota: Si se desea garantizar atomicidad en operaciones de escritura,
 * puede anotarse con {@code @Transactional} a nivel de método o clase.</p>
 */
@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    /** Repositorio de persistencia de clientes. */
    private final ClienteRepository repo;

    /** Repositorio de ventas, usado para construir el historial por correo. */
    private final VentaRepository ventaRepo;

    /**
     * Registra un nuevo cliente validando obligatoriedad y unicidad de correo.
     *
     * @param dto datos del cliente a registrar.
     * @return cliente creado como {@link ClienteDto}.
     * @throws IllegalArgumentException si faltan campos requeridos o el correo ya existe.
     */
    @Override
    public ClienteDto registrar(ClienteDto dto) {
        validar(dto);
        if (repo.existsByCorreo(dto.getCorreo())) {
            throw new IllegalArgumentException("Ya existe un cliente con ese correo.");
        }
        Cliente entity = Cliente.builder()
                .nombre(dto.getNombre())
                .correo(dto.getCorreo())
                .build();
        entity = repo.save(entity);
        return toDto(entity);
    }

    /**
     * Lista todos los clientes registrados.
     *
     * @return lista de {@link ClienteDto}.
     */
    @Override
    public List<ClienteDto> listar() {
        return repo.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    /**
     * Obtiene un cliente por su identificador.
     *
     * @param id identificador del cliente.
     * @return cliente encontrado como {@link ClienteDto}.
     * @throws IllegalArgumentException si el cliente no existe.
     */
    @Override
    public ClienteDto obtenerPorId(Long id) {
        Cliente c = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El cliente no existe."));
        return toDto(c);
    }

    /**
     * Actualiza los datos de un cliente.
     *
     * <p>Valida obligatoriedad y evita duplicidad del correo con otros clientes.</p>
     *
     * @param id  identificador del cliente a actualizar.
     * @param dto datos nuevos (nombre y correo).
     * @return cliente actualizado como {@link ClienteDto}.
     * @throws IllegalArgumentException si el cliente no existe o el correo ya está en uso por otro.
     */
    @Override
    public ClienteDto actualizar(Long id, ClienteDto dto) {
        validar(dto);
        Cliente c = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El cliente no existe."));
        if (!c.getCorreo().equals(dto.getCorreo()) && repo.existsByCorreo(dto.getCorreo())) {
            throw new IllegalArgumentException("El correo ya está registrado por otro cliente.");
        }
        c.setNombre(dto.getNombre());
        c.setCorreo(dto.getCorreo());
        c = repo.save(c);
        return toDto(c);
    }

    /**
     * Elimina un cliente por su identificador.
     *
     * @param id identificador del cliente a eliminar.
     * @throws IllegalArgumentException si el cliente no existe.
     */
    @Override
    public void eliminar(Long id) {
        if (!repo.existsById(id)) {
            throw new IllegalArgumentException("El cliente no existe.");
        }
        repo.deleteById(id);
    }

    // ===========================
    // Historial por correo
    // ===========================

    /**
     * Obtiene el historial de compras (billetes vendidos) de un cliente por su correo electrónico.
     *
     * <p>Construye un {@link HistorialClienteDto} con los datos del cliente y
     * un resumen de los billetes (incluyendo sorteo si aplica).</p>
     *
     * @param correo correo del cliente a consultar.
     * @return resumen del historial del cliente.
     * @throws ResponseStatusException (404) si el cliente no existe.
     */
    @Override
    public HistorialClienteDto obtenerHistorialPorCorreo(String correo) {
        var cliente = repo.findByCorreo(correo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));

        var ventas = ventaRepo.findAllByClienteCorreoOrderByIdDesc(correo);

        var billetes = ventas.stream()
                .map(v -> {
                    Billete b = v.getBillete();
                    return HistorialClienteDto.BilleteResumen.builder()
                            .id(b.getId())
                            .numero(b.getNumero())
                            .precio(b.getPrecio())
                            .estado(b.getEstado() != null ? b.getEstado().name() : null)
                            .sorteoId(b.getSorteo() != null ? b.getSorteo().getId() : null)
                            .sorteoNombre(b.getSorteo() != null ? b.getSorteo().getNombre() : null)
                            .build();
                })
                .toList();

        return HistorialClienteDto.builder()
                .id(cliente.getId())
                .nombre(cliente.getNombre())
                .correo(cliente.getCorreo())
                .billetes(billetes)
                .build();
    }

    // ---- helpers ----

    /**
     * Valida reglas mínimas del DTO de cliente (obligatoriedad de nombre y correo).
     *
     * @param dto datos a validar.
     * @throws IllegalArgumentException si alguno de los campos obligatorios está vacío o nulo.
     */
    private void validar(ClienteDto dto) {
        if (dto.getNombre() == null || dto.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (dto.getCorreo() == null || dto.getCorreo().isBlank()) {
            throw new IllegalArgumentException("El correo es obligatorio.");
        }
    }

    /**
     * Convierte una entidad {@link Cliente} a {@link ClienteDto}.
     *
     * @param c entidad persistida.
     * @return representación DTO.
     */
    private ClienteDto toDto(Cliente c) {
        ClienteDto dto = new ClienteDto();
        dto.setId(c.getId());
        dto.setNombre(c.getNombre());
        dto.setCorreo(c.getCorreo());
        return dto;
    }
}
