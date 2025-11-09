package org.konex.sistemaloteria.cliente.service;

import org.konex.sistemaloteria.cliente.dto.ClienteDto;
import org.konex.sistemaloteria.cliente.dto.HistorialClienteDto;

import java.util.List;

/**
 * Interfaz que define el contrato para la gestión de clientes.
 *
 * <p>
 * Este servicio forma parte del módulo de clientes del
 * <b>Sistema de Ventas de Lotería</b> desarrollado para la
 * prueba técnica de Konex Innovation:contentReference[oaicite:1]{index=1}.
 * </p>
 *
 * <p>
 * Se encarga de las operaciones básicas sobre la entidad Cliente:
 * <ul>
 *   <li>Registrar nuevos clientes.</li>
 *   <li>Listar todos los clientes registrados.</li>
 *   <li>Obtener, actualizar o eliminar un cliente específico.</li>
 *   <li>Consultar el historial de billetes vendidos asociados a un cliente (por correo electrónico).</li>
 * </ul>
 * </p>
 *
 * <p>
 * La implementación por defecto es {@link org.konex.sistemaloteria.cliente.service.ClienteServiceImpl}.
 * </p>
 */
public interface ClienteService {

    /**
     * Registra un nuevo cliente en el sistema.
     *
     * @param dto objeto {@link ClienteDto} con el nombre y correo del cliente.
     * @return el cliente creado en formato {@link ClienteDto}.
     */
    ClienteDto registrar(ClienteDto dto);

    /**
     * Obtiene el listado de todos los clientes registrados.
     *
     * @return una lista de objetos {@link ClienteDto}.
     */
    List<ClienteDto> listar();

    /**
     * Recupera los datos de un cliente por su identificador.
     *
     * @param id identificador único del cliente.
     * @return un {@link ClienteDto} con la información del cliente.
     */
    ClienteDto obtenerPorId(Long id);

    /**
     * Actualiza la información de un cliente existente.
     *
     * @param id identificador del cliente a actualizar.
     * @param dto datos nuevos (nombre y correo).
     * @return el cliente actualizado como {@link ClienteDto}.
     */
    ClienteDto actualizar(Long id, ClienteDto dto);

    /**
     * Elimina un cliente del sistema por su identificador.
     *
     * @param id identificador del cliente a eliminar.
     */
    void eliminar(Long id);

    /**
     * Obtiene el historial de billetes vendidos de un cliente usando su correo electrónico.
     *
     * <p>
     * Devuelve un {@link HistorialClienteDto} que incluye:
     * <ul>
     *   <li>Datos del cliente (id, nombre, correo).</li>
     *   <li>Lista de billetes vendidos, con número, precio, estado y sorteo asociado.</li>
     * </ul>
     * </p>
     *
     * @param correo correo electrónico del cliente a consultar.
     * @return objeto {@link HistorialClienteDto} con el historial completo del cliente.
     */
    HistorialClienteDto obtenerHistorialPorCorreo(String correo);
}
