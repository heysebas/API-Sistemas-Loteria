package org.konex.sistemaloteria.billete.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.konex.sistemaloteria.cliente.model.Cliente;
import org.konex.sistemaloteria.compartido.EstadoBillete;
import org.konex.sistemaloteria.sorteo.model.Sorteo;

import java.math.BigDecimal;

/**
 * Entidad que representa un billete dentro del sistema de ventas de lotería.
 *
 * <p>
 * Cada billete pertenece a un sorteo y puede estar asociado opcionalmente
 * a un cliente cuando ha sido vendido. Su estado se define por la enumeración
 * {@link EstadoBillete}, con valores posibles {@code DISPONIBLE} y {@code VENDIDO}.
 * </p>
 *
 * <p>
 * La tabla {@code billetes} define unicidad por combinación de sorteo y número,
 * lo que garantiza que dentro de un mismo sorteo no se repita el mismo número de billete.
 * </p>
 *
 * <p>
 * Forma parte del modelo de dominio implementado para la
 * <b>Prueba Técnica – Sistema de Ventas de Lotería</b> de Konex Innovation:contentReference[oaicite:1]{index=1}.
 * </p>
 */
@Entity
@Table(
        name = "billetes",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "ux_billetes_sorteo_numero",
                        columnNames = {"sorteo_id", "numero"}
                )
        },
        indexes = {
                @Index(name = "ix_billetes_sorteo", columnList = "sorteo_id"),
                @Index(name = "ix_billetes_cliente", columnList = "cliente_id")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Billete {

    /** Identificador único del billete (clave primaria autogenerada). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Número del billete dentro de un sorteo.
     *
     * <p>
     * La combinación {@code (sorteo_id, numero)} debe ser única.
     * No se usa {@code unique=true} a nivel de columna porque la restricción
     * se aplica a nivel de tabla mediante {@link UniqueConstraint}.
     * </p>
     */
    @Column(name = "numero", nullable = false, length = 16)
    private String numero;

    /** Precio del billete (valor de venta). */
    @Column(name = "precio", nullable = false, precision = 12, scale = 2)
    private BigDecimal precio;

    /**
     * Estado actual del billete.
     * <ul>
     *   <li>{@link EstadoBillete#DISPONIBLE}: billete libre para venta.</li>
     *   <li>{@link EstadoBillete#VENDIDO}: billete ya asignado a un cliente.</li>
     * </ul>
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 16)
    @Builder.Default
    private EstadoBillete estado = EstadoBillete.DISPONIBLE;

    /**
     * Relación con el sorteo al que pertenece el billete.
     *
     * <p>
     * Se define como {@code ManyToOne} y es obligatoria.
     * Usa {@link JsonBackReference} para evitar recursión infinita
     * al serializar la relación {@code Sorteo → Billete}.
     * </p>
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sorteo_id", nullable = false)
    @ToString.Exclude
    @JsonBackReference("sorteo-billetes")
    private Sorteo sorteo;

    /**
     * Cliente que compró el billete (si aplica).
     *
     * <p>
     * La relación es opcional: un billete puede no estar asignado a ningún cliente
     * mientras se encuentra en estado {@code DISPONIBLE}.
     * </p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    @ToString.Exclude
    @JsonIgnoreProperties({"billetes", "hibernateLazyInitializer", "handler"})
    private Cliente cliente;
}
