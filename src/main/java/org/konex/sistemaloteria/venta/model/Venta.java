package org.konex.sistemaloteria.venta.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.konex.sistemaloteria.billete.model.Billete;
import org.konex.sistemaloteria.cliente.model.Cliente;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ventas", uniqueConstraints = {
        @UniqueConstraint(columnNames = "billete_id") // un billete solo se puede vender una vez
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "billete_id", nullable = false)
    @ToString.Exclude
    private Billete billete;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    @ToString.Exclude
    private Cliente cliente;

    @Column(name = "fecha_venta", nullable = false)
    private LocalDateTime fechaVenta;

    @Column(nullable = false)
    private BigDecimal precio;
}
