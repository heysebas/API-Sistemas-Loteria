package org.konex.sistemaloteria.billete.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.konex.sistemaloteria.compartido.EstadoBillete;
import org.konex.sistemaloteria.sorteo.model.Sorteo;
import org.konex.sistemaloteria.cliente.model.Cliente;

import java.math.BigDecimal;

@Entity
@Table(name = "billetes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Billete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numero;

    @Column(nullable = false)
    private BigDecimal precio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private EstadoBillete estado = EstadoBillete.DISPONIBLE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sorteo_id", nullable = false)
    @ToString.Exclude
    private Sorteo sorteo;

    // Cliente comprador (null si aún no se ha vendido)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    @ToString.Exclude
    private Cliente cliente;
}
