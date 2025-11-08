package org.konex.sistemaloteria.sorteo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class SorteoDto {

    private Long id;

    @NotBlank(message = "El nombre del sorteo es obligatorio")
    private String nombre;

    @NotNull(message = "La fecha del sorteo es obligatoria")
    private LocalDate fechaSorteo;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public LocalDate getFechaSorteo() { return fechaSorteo; }
    public void setFechaSorteo(LocalDate fechaSorteo) { this.fechaSorteo = fechaSorteo; }
}
