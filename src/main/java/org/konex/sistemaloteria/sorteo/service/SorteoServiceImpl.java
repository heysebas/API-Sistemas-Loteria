package org.konex.sistemaloteria.sorteo.service;

import lombok.RequiredArgsConstructor;
import org.konex.sistemaloteria.billete.model.Billete;
import org.konex.sistemaloteria.billete.repository.BilleteRepository;
import org.konex.sistemaloteria.cliente.dto.ClienteDto;
import org.konex.sistemaloteria.compartido.EstadoBillete;
import org.konex.sistemaloteria.sorteo.dto.SorteoDto;
import org.konex.sistemaloteria.sorteo.model.Sorteo;
import org.konex.sistemaloteria.sorteo.repository.SorteoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio que implementa la lógica de negocio relacionada con los sorteos.
 *
 * <p>
 * Encargado de:
 * <ul>
 *   <li>Crear nuevos sorteos.</li>
 *   <li>Listar sorteos existentes.</li>
 *   <li>Generar los billetes asociados a un sorteo.</li>
 *   <li>Consultar los billetes de un sorteo específico, incluyendo el cliente comprador.</li>
 * </ul>
 *
 * <p>
 * Este servicio forma parte del módulo de backend definido en la
 * <b>Prueba Técnica - Sistema de Ventas de Lotería</b> de Konex Innovation.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class SorteoServiceImpl implements SorteoService {

    /** Repositorio para operaciones CRUD sobre la entidad {@link Sorteo}. */
    private final SorteoRepository sorteoRepository;

    /** Repositorio para la persistencia de billetes asociados a los sorteos. */
    private final BilleteRepository billeteRepository;

    /** Mapper usado para convertir entre entidades y DTOs. */
    private final ModelMapper mapper;

    /**
     * Crea un nuevo sorteo a partir de un DTO recibido.
     *
     * @param dto objeto con los datos del sorteo (nombre y fecha).
     * @return un {@link SorteoDto} con la información del sorteo guardado.
     */
    @Override
    public SorteoDto crear(SorteoDto dto) {
        Sorteo sorteo = mapper.map(dto, Sorteo.class);
        sorteo = sorteoRepository.save(sorteo);
        return mapper.map(sorteo, SorteoDto.class);
    }

    /**
     * Lista todos los sorteos registrados en la base de datos.
     *
     * @return una lista de objetos {@link SorteoDto}.
     */
    @Override
    public List<SorteoDto> listar() {
        return sorteoRepository.findAll().stream()
                .map(s -> mapper.map(s, SorteoDto.class))
                .collect(Collectors.toList());
    }

    /**
     * Genera un conjunto de billetes para un sorteo específico.
     *
     * <p>
     * Cada billete se numera secuencialmente (por ejemplo, 0001, 0002...),
     * se marca como <b>DISPONIBLE</b> y se asocia al sorteo indicado.
     * </p>
     *
     * @param sorteoId identificador del sorteo al cual se agregarán los billetes.
     * @param cantidad cantidad total de billetes a generar.
     * @param precio valor unitario de cada billete.
     * @return lista de {@link Billete} creados y persistidos.
     * @throws RuntimeException si el sorteo no existe.
     */
    @Override
    public List<Billete> generarBilletes(Long sorteoId, int cantidad, double precio) {
        Sorteo sorteo = sorteoRepository.findById(sorteoId)
                .orElseThrow(() -> new RuntimeException("Sorteo no encontrado con ID: " + sorteoId));

        List<Billete> billetes = new ArrayList<>();

        for (int i = 1; i <= cantidad; i++) {
            Billete billete = new Billete();
            billete.setNumero(String.format("%04d", i));
            billete.setPrecio(BigDecimal.valueOf(precio));
            billete.setEstado(EstadoBillete.DISPONIBLE);
            billete.setSorteo(sorteo);
            billetes.add(billeteRepository.save(billete));
        }

        return billetes;
    }

    /**
     * Obtiene todos los billetes registrados para un sorteo dado,
     * incluyendo la información básica del cliente comprador si existe.
     *
     * @param sorteoId identificador del sorteo.
     * @return lista de billetes asociados al sorteo, con cliente (si aplica).
     */
    @Override
    public List<Billete> listarBilletesPorSorteo(Long sorteoId) {
        List<Billete> billetes = billeteRepository.findBySorteoId(sorteoId);

        // 🔹 Enriquecer manualmente con los datos del cliente (nombre y correo)
        for (Billete b : billetes) {
            if (b.getCliente() != null) {
                // Forzar carga de cliente si es proxy (por Lazy Fetch)
                b.getCliente().getNombre();
                b.getCliente().getCorreo();
            }
        }

        return billetes;
    }
}
