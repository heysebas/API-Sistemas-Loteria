package org.konex.sistemaloteria.venta.repository;

import org.konex.sistemaloteria.venta.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repositorio JPA para la entidad {@link Venta}.
 *
 * Proporciona operaciones CRUD básicas (heredadas de {@link JpaRepository})
 * y consultas personalizadas para recuperar ventas por cliente o por correo electrónico.
 *
 * <p>
 * Las consultas personalizadas usan {@code JOIN FETCH} para evitar el problema
 * de carga diferida (LazyInitializationException) al acceder a relaciones como
 * {@code billete}, {@code sorteo} o {@code cliente}.
 * </p>
 */
public interface VentaRepository extends JpaRepository<Venta, Long> {

    /**
     * Busca todas las ventas asociadas a un cliente específico.
     *
     * @param clienteId identificador único del cliente.
     * @return lista de ventas correspondientes al cliente.
     */
    List<Venta> findByClienteId(Long clienteId);

    /**
     * Busca todas las ventas realizadas por un cliente usando su correo electrónico.
     *
     * <p>
     * La consulta incluye una carga anticipada ({@code JOIN FETCH}) de:
     * <ul>
     *   <li>{@code v.billete}: billete vendido.</li>
     *   <li>{@code b.sorteo}: sorteo al que pertenece el billete.</li>
     *   <li>{@code v.cliente}: datos del cliente asociado.</li>
     * </ul>
     * Se ordenan los resultados de forma descendente por ID (ventas más recientes primero).
     * </p>
     *
     * @param correo correo electrónico del cliente.
     * @return lista de ventas con billete y sorteo asociados.
     */
    @Query("""
           SELECT v
           FROM Venta v
           JOIN FETCH v.billete b
           LEFT JOIN FETCH b.sorteo s
           JOIN FETCH v.cliente c
           WHERE c.correo = :correo
           ORDER BY v.id DESC
           """)
    List<Venta> findAllByClienteCorreoOrderByIdDesc(String correo);
}
