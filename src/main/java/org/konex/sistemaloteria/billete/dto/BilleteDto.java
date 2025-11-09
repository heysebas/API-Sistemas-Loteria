package org.konex.sistemaloteria.billete.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import org.konex.sistemaloteria.compartido.EstadoBillete;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BilleteDto {

    private Long id;

    @NotBlank(message = "numero es obligatorio")
    @Pattern(regexp = "\\d{1,6}", message = "numero debe tener entre 1 y 6 dígitos numéricos")
    private String numero;

    @NotNull(message = "precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "precio debe ser mayor a 0")
    private BigDecimal precio;

    private EstadoBillete estado;

    @NotNull(message = "sorteoId es obligatorio")
    private Long sorteoId;
}
