package org.konex.sistemaloteria.sorteo.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.konex.sistemaloteria.billete.model.Billete;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa un sorteo dentro del sistema de ventas de lotería.
 *
 * <p>
 * Un sorteo agrupa un conjunto de billetes disponibles para la venta
 * y contiene información básica como su nombre y fecha de realización.
 * </p>
 *
 * <p>
 * Cada sorteo mantiene una relación uno a muchos con {@link Billete},
 * lo que permite registrar y consultar todos los billetes asociados.
 * </p>
 *
 * <p>
 * Forma parte del modelo de dominio especificado en la
 * <b>Prueba Técnica – Sistema de Ventas de Lotería</b> de Konex Innovation:contentReference[oaicite:1]{index=1}.
 * </p>
 */
@Entity
@Table(name = "sorteos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Sorteo {

    /** Identificador único del sorteo. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre o título descriptivo del sorteo (por ejemplo, "Sorteo de Navidad"). */
    @Column(nullable = false)
    private String nombre;

    /** Fecha programada para la realización del sorteo. */
    @Column(name = "fecha_sorteo", nullable = false)
    private LocalDate fechaSorteo;

    /**
     * Lista de billetes asociados a este sorteo.
     *
     * <p>
     * La relación es <b>uno a muchos</b>: un sorteo puede tener múltiples billetes.
     * Se utiliza {@link JsonManagedReference} para manejar correctamente
     * la serialización bidireccional con {@link Billete}.
     * </p>
     *
     * <p>
     * Configuración adicional:
     * <ul>
     *   <li>{@code cascade = CascadeType.ALL}: al eliminar un sorteo, se eliminan sus billetes.</li>
     *   <li>{@code orphanRemoval = true}: elimina billetes huérfanos que pierdan su relación.</li>
     * </ul>
     * </p>
     */
    @OneToMany(mappedBy = "sorteo", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("sorteo-billetes")
    private List<Billete> billetes = new ArrayList<>();
}
