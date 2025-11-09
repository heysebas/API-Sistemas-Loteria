package org.konex.sistemaloteria.venta.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.konex.sistemaloteria.billete.model.Billete;
import org.konex.sistemaloteria.cliente.model.Cliente;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad que representa una venta registrada en el sistema.
 *
 * Cada venta corresponde a la transacción de un {@link Billete} adquirido
 * por un {@link Cliente}, incluyendo la fecha y el precio de venta.
 *
 * <p>
 * Reglas principales:
 * <ul>
 *   <li>Un billete solo puede estar asociado a una venta (restricción única).</li>
 *   <li>Una venta siempre tiene un cliente y un billete válidos (campos no nulos).</li>
 * </ul>
 *
 * <p>
 * Esta clase usa anotaciones de <b>Lombok</b> para generar automáticamente
 * los métodos getters/setters, constructores y {@code toString()}.
 * </p>
 */
@Entity
@Table(
        name = "ventas",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "billete_id") // Un billete solo puede venderse una vez
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Venta {

    /** Identificador único de la venta. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Billete vendido en esta transacción.
     *
     * Relación uno a uno:
     * - Un billete solo puede aparecer en una venta.
     * - Carga diferida (LAZY) para optimizar rendimiento.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "billete_id", nullable = false)
    @ToString.Exclude
    private Billete billete;

    /**
     * Cliente que realizó la compra del billete.
     *
     * Relación muchos a uno:
     * - Un cliente puede tener múltiples ventas.
     * - Carga diferida (LAZY) para evitar consultas innecesarias.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    @ToString.Exclude
    private Cliente cliente;

    /**
     * Fecha y hora exacta en la que se registró la venta.
     */
    @Column(name = "fecha_venta", nullable = false)
    private LocalDateTime fechaVenta;

    /**
     * Precio total de la venta, correspondiente al valor del billete.
     */
    @Column(nullable = false)
    private BigDecimal precio;
}
