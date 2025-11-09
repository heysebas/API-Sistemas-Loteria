package org.konex.sistemaloteria.cliente.service;

import org.konex.sistemaloteria.cliente.dto.ClienteDto;
import org.konex.sistemaloteria.cliente.dto.HistorialClienteDto;

import java.util.List;

/**
 * Interfaz que define las operaciones de negocio relacionadas con clientes.
 */
public interface ClienteService {

    /** Crea un nuevo cliente validando obligatoriedad y duplicados. */
    ClienteDto crearCliente(ClienteDto dto);

    /** Lista todos los clientes registrados. */
    List<ClienteDto> listar();

    /** Obtiene un cliente por su identificador. */
    ClienteDto obtenerPorId(Long id);

    /** Actualiza los datos de un cliente existente. */
    ClienteDto actualizar(Long id, ClienteDto dto);

    /** Elimina un cliente por su identificador. */
    void eliminar(Long id);

    /** Devuelve el historial de billetes comprados por correo del cliente. */
    HistorialClienteDto historialPorCorreo(String correo);
}
