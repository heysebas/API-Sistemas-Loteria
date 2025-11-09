package org.konex.sistemaloteria.billete.service;

import java.util.List;
import org.konex.sistemaloteria.billete.dto.BilleteDto;

/**
 * Interfaz que define el contrato del servicio para la gestión de billetes.
 *
 * <p>
 * Esta capa abstrae la lógica de negocio asociada a los billetes
 * dentro del <b>Sistema de Ventas de Lotería</b>, incluyendo su creación
 * y su consulta por sorteo.
 * </p>
 *
 * <p>
 * La implementación por defecto es {@link org.konex.sistemaloteria.billete.service.BilleteServiceImpl}.
 * </p>
 *
 * <h4>Responsabilidades:</h4>
 * <ul>
 *   <li>Crear nuevos billetes asociados a un sorteo.</li>
 *   <li>Listar billetes filtrados por identificador de sorteo.</li>
 * </ul>
 *
 * <h4>Ejemplo de uso:</h4>
 * <pre>
 * BilleteDto nuevo = billeteService.crearBillete(
 *     new BilleteDto(null, "0008", new BigDecimal("10000"), EstadoBillete.DISPONIBLE, 1L)
 * );
 *
 * List&lt;BilleteDto&gt; billetes = billeteService.listarPorSorteo(1L);
 * </pre>
 */
public interface BilleteService {

    /**
     * Crea un nuevo billete asociado a un sorteo existente.
     *
     * <p>
     * Si el estado del billete no se especifica en el DTO, se asigna por defecto
     * el valor {@code DISPONIBLE}.
     * </p>
     *
     * @param dto objeto {@link BilleteDto} con los datos del nuevo billete.
     * @return el billete creado con su ID y estado actualizados.
     * @throws RuntimeException si el sorteo asociado no existe.
     */
    BilleteDto crearBillete(BilleteDto dto);

    /**
     * Lista todos los billetes correspondientes a un sorteo específico.
     *
     * <p>
     * Devuelve información básica de cada billete: número, precio, estado y sorteoId.
     * </p>
     *
     * @param sorteoId identificador único del sorteo.
     * @return lista de objetos {@link BilleteDto}.
     */
    List<BilleteDto> listarPorSorteo(Long sorteoId);
}
