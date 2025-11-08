package org.konex.sistemaloteria.billete.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.konex.sistemaloteria.billete.dto.BilleteDto;
import org.konex.sistemaloteria.billete.model.Billete;
import org.konex.sistemaloteria.billete.repository.BilleteRepository;
import org.konex.sistemaloteria.compartido.EstadoBillete;
import org.konex.sistemaloteria.sorteo.model.Sorteo;
import org.konex.sistemaloteria.sorteo.repository.SorteoRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BilleteServiceImpl implements BilleteService {

    private final BilleteRepository billeteRepo;
    private final SorteoRepository sorteoRepo;

    @Override
    public BilleteDto crearBillete(BilleteDto dto) {
        // Buscar el sorteo asociado al billete
        Sorteo sorteo = sorteoRepo.findById(dto.getSorteoId())
                .orElseThrow(() -> new RuntimeException("El sorteo con ID " + dto.getSorteoId() + " no existe."));

        // Crear el billete usando el builder de Lombok
        Billete billete = Billete.builder()
                .numero(dto.getNumero())
                .precio(dto.getPrecio())
                .estado(dto.getEstado() != null ? dto.getEstado() : EstadoBillete.DISPONIBLE)
                .sorteo(sorteo)
                .build();

        // Guardar en base de datos
        Billete guardado = billeteRepo.save(billete);

        // Devolver el DTO actualizado con el ID y estado
        dto.setId(guardado.getId());
        dto.setEstado(guardado.getEstado());
        return dto;
    }

    @Override
    public List<BilleteDto> listarPorSorteo(Long sorteoId) {
        return billeteRepo.findBySorteoId(sorteoId)
                .stream()
                .map(b -> {
                    BilleteDto dto = new BilleteDto();
                    dto.setId(b.getId());
                    dto.setNumero(b.getNumero());
                    dto.setPrecio(b.getPrecio());
                    dto.setEstado(b.getEstado());
                    dto.setSorteoId(b.getSorteo().getId());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
