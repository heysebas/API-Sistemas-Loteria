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

/**
 * Servicio que gestiona las operaciones relacionadas con los billetes.
 *
 * <p>
 * Implementa la interfaz {@link BilleteService} y contiene la lógica
 * de negocio asociada a la creación y consulta de billetes
 * dentro de un sorteo específico.
 * </p>
 *
 * <p>
 * Forma parte del módulo <b>Billete</b> del
 * <b>Sistema de Ventas de Lotería</b> desarrollado para la
 * <b>Prueba Técnica – Konex Innovation</b>:contentReference[oaicite:1]{index=1}.
 * </p>
 *
 * <h4>Responsabilidades principales:</h4>
 * <ul>
 *   <li>Crear nuevos billetes asociados a un sorteo existente.</li>
 *   <li>Listar billetes filtrados por sorteo.</li>
 *   <li>Inicializar billetes con estado {@code DISPONIBLE} por defecto.</li>
 * </ul>
 *
 * <p>
 * En caso de sorteo inexistente, lanza una excepción {@link RuntimeException}.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class BilleteServiceImpl implements BilleteService {

    /** Repositorio para operaciones de persistencia de billetes. */
    private final BilleteRepository billeteRepo;

    /** Repositorio de sorteos, usado para validar la existencia de sorteos asociados. */
    private final SorteoRepository sorteoRepo;

    /**
     * Crea un nuevo billete asociado a un sorteo existente.
     *
     * <p>
     * Si el estado no se especifica en el DTO, el billete se inicializa con
     * {@link EstadoBillete#DISPONIBLE} por defecto.
     * </p>
     *
     * <h4>Ejemplo de solicitud JSON:</h4>
     * <pre>
     * {
     *   "numero": "0005",
     *   "precio": 10000,
     *   "estado": "DISPONIBLE",
     *   "sorteoId": 2
     * }
     * </pre>
     *
     * <p>
     * Si el sorteo asociado no existe, se lanza una excepción con mensaje claro.
     * </p>
     *
     * @param dto objeto {@link BilleteDto} con los datos del nuevo billete.
     * @return el billete creado con su ID y estado actualizados.
     */
    @Override
    public BilleteDto crearBillete(BilleteDto dto) {
        // Buscar el sorteo asociado al billete
        Sorteo sorteo = sorteoRepo.findById(dto.getSorteoId())
                .orElseThrow(() -> new RuntimeException("El sorteo con ID " + dto.getSorteoId() + " no existe."));

        // Crear el billete usando el patrón Builder de Lombok
        Billete billete = Billete.builder()
                .numero(dto.getNumero())
                .precio(dto.getPrecio())
                .estado(dto.getEstado() != null ? dto.getEstado() : EstadoBillete.DISPONIBLE)
                .sorteo(sorteo)
                .build();

        // Guardar en base de datos
        Billete guardado = billeteRepo.save(billete);

        // Devolver el DTO actualizado con el ID y estado persistidos
        dto.setId(guardado.getId());
        dto.setEstado(guardado.getEstado());
        return dto;
    }

    /**
     * Lista todos los billetes pertenecientes a un sorteo específico.
     *
     * <p>
     * Devuelve únicamente los billetes vinculados al {@code sorteoId} indicado,
     * incluyendo número, precio, estado y referencia al sorteo.
     * </p>
     *
     * <h4>Ejemplo de uso:</h4>
     * <pre>GET /api/sorteos/3/billetes</pre>
     *
     * @param sorteoId identificador del sorteo.
     * @return lista de billetes en formato {@link BilleteDto}.
     */
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
