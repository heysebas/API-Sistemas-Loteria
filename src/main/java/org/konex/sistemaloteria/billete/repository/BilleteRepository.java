package org.konex.sistemaloteria.billete.repository;

import org.konex.sistemaloteria.billete.model.Billete;
import org.konex.sistemaloteria.compartido.EstadoBillete;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio JPA para la entidad {@link Billete}.
 *
 * <p>
 * Proporciona las operaciones CRUD básicas (crear, leer, actualizar, eliminar)
 * mediante la herencia de {@link JpaRepository}, además de consultas
 * personalizadas específicas para billetes asociados a sorteos o clientes.
 * </p>
 *
 * <p>
 * Forma parte del módulo <b>Billete</b> del
 * <b>Sistema de Ventas de Lotería</b> desarrollado para la
 * <b>Prueba Técnica – Konex Innovation</b>:contentReference[oaicite:1]{index=1}.
 * </p>
 *
 * <h4>Consultas personalizadas incluidas:</h4>
 * <ul>
 *   <li>{@link #findBySorteoId(Long)} — obtiene todos los billetes de un sorteo.</li>
 *   <li>{@link #findBySorteoIdAndEstado(Long, EstadoBillete)} — filtra billetes por sorteo y estado (ej. DISPONIBLE o VENDIDO).</li>
 *   <li>{@link #findByClienteId(Long)} — lista billetes asociados a un cliente específico.</li>
 * </ul>
 *
 * <h4>Ejemplo de uso:</h4>
 * <pre>
 * List&lt;Billete&gt; disponibles = billeteRepository.findBySorteoIdAndEstado(1L, EstadoBillete.DISPONIBLE);
 * List&lt;Billete&gt; vendidos = billeteRepository.findByClienteId(3L);
 * </pre>
 */
public interface BilleteRepository extends JpaRepository<Billete, Long> {

    /**
     * Obtiene todos los billetes pertenecientes a un sorteo específico.
     *
     * @param sorteoId identificador del sorteo.
     * @return lista de billetes asociados a ese sorteo.
     */
    List<Billete> findBySorteoId(Long sorteoId);

    /**
     * Obtiene todos los billetes de un sorteo filtrados por su estado.
     *
     * <p>
     * Útil para listar billetes disponibles o vendidos dentro de un mismo sorteo.
     * </p>
     *
     * @param sorteoId identificador del sorteo.
     * @param estado estado del billete ({@link EstadoBillete#DISPONIBLE} o {@link EstadoBillete#VENDIDO}).
     * @return lista de billetes filtrados por sorteo y estado.
     */
    List<Billete> findBySorteoIdAndEstado(Long sorteoId, EstadoBillete estado);

    /**
     * Obtiene los billetes comprados por un cliente específico.
     *
     * <p>
     * Utilizado principalmente por el módulo de historial de clientes
     * para mostrar los billetes vendidos asociados a un cliente.
     * </p>
     *
     * @param clienteId identificador único del cliente.
     * @return lista de billetes comprados por ese cliente.
     */
    List<Billete> findByClienteId(Long clienteId);
}
