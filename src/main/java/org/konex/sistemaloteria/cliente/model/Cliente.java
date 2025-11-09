package org.konex.sistemaloteria.cliente.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.konex.sistemaloteria.billete.model.Billete;

import java.util.List;

/**
 * Entidad que representa un cliente dentro del sistema de ventas de lotería.
 *
 * <p>
 * Cada cliente puede estar asociado a múltiples billetes vendidos,
 * los cuales se registran en la relación uno a muchos con {@link Billete}.
 * </p>
 *
 * <p>
 * Las instancias de esta entidad se almacenan en la tabla <b>clientes</b>,
 * con una restricción de unicidad sobre el campo {@code correo}
 * para evitar registros duplicados.
 * </p>
 *
 * <p>
 * Forma parte del modelo de dominio definido en la
 * <b>Prueba Técnica – Sistema de Ventas de Lotería</b> de Konex Innovation:contentReference[oaicite:1]{index=1}.
 * </p>
 */
@Entity
@Table(
        name = "clientes",
        uniqueConstraints = @UniqueConstraint(columnNames = "correo")
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Cliente {

    /** Identificador único del cliente (clave primaria autogenerada). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre completo del cliente. Campo obligatorio. */
    @Column(nullable = false)
    private String nombre;

    /** Correo electrónico del cliente. Debe ser único y obligatorio. */
    @Column(nullable = false)
    private String correo;

    /**
     * Lista de billetes comprados por el cliente.
     *
     * <p>
     * Relación uno a muchos con la entidad {@link Billete}.
     * Se usa {@link JsonIgnore} para evitar recursión infinita al serializar
     * y {@code fetch = LAZY} para no cargar los billetes innecesariamente
     * en las consultas de cliente.
     * </p>
     */
    @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private List<Billete> billetes;
}
