package org.konex.sistemaloteria.sorteo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * DTO que representa los datos de transferencia para la entidad {@code Sorteo}.
 *
 * <p>
 * Se utiliza tanto para la creación de nuevos sorteos como para su visualización.
 * Contiene los campos esenciales definidos en los requisitos de la prueba técnica:
 * <ul>
 *   <li>Nombre del sorteo</li>
 *   <li>Fecha del sorteo</li>
 * </ul>
 * </p>
 *
 * <p>
 * Incluye validaciones de campos mediante anotaciones de {@link jakarta.validation}:
 * <ul>
 *   <li>{@link NotBlank}: el nombre no puede estar vacío o nulo.</li>
 *   <li>{@link NotNull}: la fecha del sorteo es obligatoria.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Ejemplo de uso en JSON (entrada/salida):
 * <pre>
 * {
 *   "id": 1,
 *   "nombre": "Sorteo de Navidad",
 *   "fechaSorteo": "2025-12-24"
 * }
 * </pre>
 * </p>
 */
public class SorteoDto {

    /** Identificador único del sorteo (autogenerado en la base de datos). */
    private Long id;

    /** Nombre descriptivo del sorteo (por ejemplo: "Sorteo de Verano Millonario"). */
    @NotBlank(message = "El nombre del sorteo es obligatorio")
    private String nombre;

    /** Fecha programada para la realización del sorteo. */
    @NotNull(message = "La fecha del sorteo es obligatoria")
    private LocalDate fechaSorteo;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public LocalDate getFechaSorteo() { return fechaSorteo; }
    public void setFechaSorteo(LocalDate fechaSorteo) { this.fechaSorteo = fechaSorteo; }
}
