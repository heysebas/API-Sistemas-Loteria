package org.konex.sistemaloteria.billete.dto;

import java.math.BigDecimal;
import org.konex.sistemaloteria.compartido.EstadoBillete;

/**
 * DTO (Data Transfer Object) que representa los datos de un billete.
 *
 * <p>
 * Se utiliza para la comunicación entre el backend y el frontend
 * en las operaciones relacionadas con los billetes:
 * <ul>
 *   <li>Creación de nuevos billetes asociados a sorteos.</li>
 *   <li>Listado de billetes disponibles o vendidos por sorteo o cliente.</li>
 * </ul>
 * </p>
 *
 * <p>
 * No incluye referencias completas a las entidades {@code Sorteo} o {@code Cliente}
 * para mantener el DTO liviano y enfocado en la transferencia de datos esenciales.
 * </p>
 *
 * <h4>Campos principales:</h4>
 * <ul>
 *   <li>{@link #id}: identificador único del billete.</li>
 *   <li>{@link #numero}: número o código del billete dentro del sorteo.</li>
 *   <li>{@link #precio}: valor del billete (por ejemplo, 10000.00).</li>
 *   <li>{@link #estado}: estado actual (DISPONIBLE o VENDIDO).</li>
 *   <li>{@link #sorteoId}: identificador del sorteo al que pertenece.</li>
 * </ul>
 *
 * <h4>Ejemplo de JSON:</h4>
 * <pre>
 * {
 *   "id": 15,
 *   "numero": "0007",
 *   "precio": 10000.00,
 *   "estado": "DISPONIBLE",
 *   "sorteoId": 3
 * }
 * </pre>
 *
 * <p>
 * Forma parte del módulo <b>Billete</b> del
 * <b>Sistema de Ventas de Lotería</b> — Prueba Técnica Konex Innovation:contentReference[oaicite:1]{index=1}.
 * </p>
 */
public class BilleteDto {

    /** Identificador único del billete (autogenerado en la base de datos). */
    private Long id;

    /** Número o código del billete (por ejemplo, "0001", "0010", etc.). */
    private String numero;

    /** Precio de venta del billete. */
    private BigDecimal precio;

    /** Estado actual del billete: DISPONIBLE o VENDIDO. */
    private EstadoBillete estado;

    /** Identificador del sorteo al que pertenece el billete. */
    private Long sorteoId;

    // --- Getters y Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public EstadoBillete getEstado() { return estado; }
    public void setEstado(EstadoBillete estado) { this.estado = estado; }

    public Long getSorteoId() { return sorteoId; }
    public void setSorteoId(Long sorteoId) { this.sorteoId = sorteoId; }
}
