package org.konex.sistemaloteria.cliente.service;

import org.konex.sistemaloteria.cliente.dto.ClienteDto;

import java.util.List;

public interface ClienteService {
    ClienteDto registrar(ClienteDto dto);
    List<ClienteDto> listar();
    ClienteDto obtenerPorId(Long id);
    ClienteDto actualizar(Long id, ClienteDto dto);
    void eliminar(Long id);
}
