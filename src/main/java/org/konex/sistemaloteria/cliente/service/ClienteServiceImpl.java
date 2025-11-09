package org.konex.sistemaloteria.cliente.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.konex.sistemaloteria.billete.model.Billete;
import org.konex.sistemaloteria.cliente.dto.ClienteDto;
import org.konex.sistemaloteria.cliente.dto.HistorialClienteDto;
import org.konex.sistemaloteria.cliente.model.Cliente;
import org.konex.sistemaloteria.cliente.repository.ClienteRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository repo;

    @Override
    public ClienteDto crearCliente(ClienteDto dto) {
        validar(dto);
        if (repo.findByCorreo(dto.getCorreo()).isPresent()) {
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
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado."));
        return toDto(c);
    }

    @Override
    public ClienteDto actualizar(Long id, ClienteDto dto) {
        validar(dto);
        Cliente c = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado."));
        if (!c.getCorreo().equals(dto.getCorreo()) && repo.findByCorreo(dto.getCorreo()).isPresent()) {
            throw new IllegalArgumentException("El correo ya está registrado por otro cliente.");
        }
        c.setNombre(dto.getNombre());
        c.setCorreo(dto.getCorreo());
        repo.save(c);
        return toDto(c);
    }

    @Override
    public void eliminar(Long id) {
        if (!repo.existsById(id)) {
            throw new IllegalArgumentException("El cliente no existe.");
        }
        repo.deleteById(id);
    }

    @Override
    public HistorialClienteDto historialPorCorreo(String correo) {
        Cliente cliente = repo.findByCorreo(correo)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado."));

        List<HistorialClienteDto.BilleteResumen> billetes = cliente.getBilletes().stream()
                .map(this::toResumen)
                .collect(Collectors.toList());

        return HistorialClienteDto.builder()
                .id(cliente.getId())
                .nombre(cliente.getNombre())
                .correo(cliente.getCorreo())
                .billetes(billetes)
                .build();
    }

    private void validar(ClienteDto dto) {
        if (dto == null) throw new IllegalArgumentException("El cuerpo de la petición es obligatorio.");
        if (dto.getNombre() == null || dto.getNombre().isBlank()) throw new IllegalArgumentException("El nombre es obligatorio.");
        if (dto.getCorreo() == null || dto.getCorreo().isBlank()) throw new IllegalArgumentException("El correo es obligatorio.");
    }

    private ClienteDto toDto(Cliente c) {
        ClienteDto dto = new ClienteDto();
        dto.setId(c.getId());
        dto.setNombre(c.getNombre());
        dto.setCorreo(c.getCorreo());
        return dto;
    }

    private HistorialClienteDto.BilleteResumen toResumen(Billete b) {
        return HistorialClienteDto.BilleteResumen.builder()
                .id(b.getId())
                .numero(b.getNumero())
                .precio(b.getPrecio())
                .estado(b.getEstado().name()) // 🔹 convertido a String
                .sorteoId(b.getSorteo() != null ? b.getSorteo().getId() : null)
                .sorteoNombre(b.getSorteo() != null ? b.getSorteo().getNombre() : null)
                .build();
    }
}
