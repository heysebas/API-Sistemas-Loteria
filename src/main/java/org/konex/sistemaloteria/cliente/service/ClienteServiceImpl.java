package org.konex.sistemaloteria.cliente.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.konex.sistemaloteria.cliente.dto.ClienteDto;
import org.konex.sistemaloteria.cliente.model.Cliente;
import org.konex.sistemaloteria.cliente.repository.ClienteRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository repo;

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

    @Override
    public List<ClienteDto> listar() {
        return repo.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public ClienteDto obtenerPorId(Long id) {
        Cliente c = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El cliente no existe."));
        return toDto(c);
    }

    @Override
    public ClienteDto actualizar(Long id, ClienteDto dto) {
        validar(dto);
        Cliente c = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El cliente no existe."));
        // si cambia el correo, verificar unicidad
        if (!c.getCorreo().equals(dto.getCorreo()) && repo.existsByCorreo(dto.getCorreo())) {
            throw new IllegalArgumentException("El correo ya está registrado por otro cliente.");
        }
        c.setNombre(dto.getNombre());
        c.setCorreo(dto.getCorreo());
        c = repo.save(c);
        return toDto(c);
    }

    @Override
    public void eliminar(Long id) {
        if (!repo.existsById(id)) {
            throw new IllegalArgumentException("El cliente no existe.");
        }
        repo.deleteById(id);
    }

    // ---- helpers ----
    private void validar(ClienteDto dto) {
        if (dto.getNombre() == null || dto.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (dto.getCorreo() == null || dto.getCorreo().isBlank()) {
            throw new IllegalArgumentException("El correo es obligatorio.");
        }
    }

    private ClienteDto toDto(Cliente c) {
        ClienteDto dto = new ClienteDto();
        dto.setId(c.getId());
        dto.setNombre(c.getNombre());
        dto.setCorreo(c.getCorreo());
        return dto;
    }
}
