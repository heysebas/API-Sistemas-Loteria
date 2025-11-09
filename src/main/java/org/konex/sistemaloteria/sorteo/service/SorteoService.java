package org.konex.sistemaloteria.sorteo.service;

import org.konex.sistemaloteria.billete.model.Billete;
import org.konex.sistemaloteria.sorteo.dto.SorteoDto;

import java.util.List;

/**
 * Interfaz que define el contrato para la gestión de sorteos.
 *
 * <p>
 * Este servicio permite crear y listar sorteos, además de manejar
 * la generación y consulta de billetes asociados a cada sorteo.
 * Forma parte del módulo backend del Sistema de Ventas de Lotería
 * especificado en la prueba técnica de Konex Innovation:contentReference[oaicite:1]{index=1}.
 * </p>
 *
 * <p>
 * Las implementaciones deben encargarse de:
 * <ul>
 *   <li>Persistir nuevos sorteos.</li>
 *   <li>Listar sorteos existentes.</li>
 *   <li>Generar billetes con numeración secuencial y estado "DISPONIBLE".</li>
 *   <li>Consultar los billetes asociados a un sorteo dado.</li>
 * </ul>
 * </p>
 *
 * Ejemplo de implementación: {@link org.konex.sistemaloteria.sorteo.service.SorteoServiceImpl}
 */
public interface SorteoService {

    /**
     * Crea un nuevo sorteo con los datos proporcionados.
     *
     * @param dto objeto {@link SorteoDto} que contiene el nombre y fecha del sorteo.
     * @return el sorteo creado como {@link SorteoDto}.
     */
    SorteoDto crear(SorteoDto dto);

    /**
     * Lista todos los sorteos registrados en el sistema.
     *
     * @return una lista de objetos {@link SorteoDto}.
     */
    List<SorteoDto> listar();

    /**
     * Genera un conjunto de billetes para un sorteo específico.
     *
     * <p>
     * Los billetes se numeran automáticamente en formato de 4 dígitos
     * (por ejemplo, 0001, 0002, …), se marcan como
     * {@code EstadoBillete.DISPONIBLE} y se asocian al sorteo indicado.
     * </p>
     *
     * @param sorteoId identificador del sorteo al cual se agregan los billetes.
     * @param cantidad cantidad total de billetes a generar.
     * @param precio valor unitario de cada billete.
     * @return lista de billetes generados.
     */
    List<Billete> generarBilletes(Long sorteoId, int cantidad, double precio);

    /**
     * Lista todos los billetes asociados a un sorteo determinado.
     *
     * @param sorteoId identificador del sorteo.
     * @return lista de billetes vinculados a ese sorteo.
     */
    List<Billete> listarBilletesPorSorteo(Long sorteoId);
}
