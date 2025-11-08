package org.konex.sistemaloteria.billete.dto;

import java.math.BigDecimal;
import org.konex.sistemaloteria.compartido.EstadoBillete;

public class BilleteDto {

    private Long id;
    private String numero;
    private BigDecimal precio;
    private EstadoBillete estado;
    private Long sorteoId;

    // Getters y Setters
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
