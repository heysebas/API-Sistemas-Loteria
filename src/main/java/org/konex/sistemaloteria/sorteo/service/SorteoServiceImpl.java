package org.konex.sistemaloteria.sorteo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.konex.sistemaloteria.sorteo.dto.SorteoDto;
import org.konex.sistemaloteria.sorteo.model.Sorteo;
import org.konex.sistemaloteria.sorteo.repository.SorteoRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SorteoServiceImpl implements SorteoService {

    private final SorteoRepository repository;

    @Override
    public SorteoDto crear(SorteoDto dto) {
        // Validar que los datos del DTO sean correctos
        if (dto.getNombre() == null || dto.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del sorteo es obligatorio.");
        }
        if (dto.getFechaSorteo() == null) {
            throw new IllegalArgumentException("La fecha del sorteo es obligatoria.");
        }

        // Crear la entidad usando Lombok Builder
        Sorteo sorteo = Sorteo.builder()
                .nombre(dto.getNombre())
                .fechaSorteo(dto.getFechaSorteo())
                .build();

        // Guardar en base de datos
        Sorteo guardado = repository.save(sorteo);

        // Convertir a DTO para devolverlo
        return convertirADto(guardado);
    }

    @Override
    public List<SorteoDto> listar() {
        return repository.findAll()
                .stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }


    private SorteoDto convertirADto(Sorteo sorteo) {
        SorteoDto dto = new SorteoDto();
        dto.setId(sorteo.getId());
        dto.setNombre(sorteo.getNombre());
        dto.setFechaSorteo(sorteo.getFechaSorteo());
        return dto;
    }
}
