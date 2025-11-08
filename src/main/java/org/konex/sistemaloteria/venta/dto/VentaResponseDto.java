package org.konex.sistemaloteria.venta.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class VentaResponseDto {
    private Long ventaId;
    private Long billeteId;
    private String numeroBillete;
    private Long clienteId;
    private String nombreCliente;
    private LocalDateTime fechaVenta;
    private BigDecimal precio;

    public VentaResponseDto(Long ventaId, Long billeteId, String numeroBillete,
                            Long clienteId, String nombreCliente,
                            LocalDateTime fechaVenta, BigDecimal precio) {
        this.ventaId = ventaId;
        this.billeteId = billeteId;
        this.numeroBillete = numeroBillete;
        this.clienteId = clienteId;
        this.nombreCliente = nombreCliente;
        this.fechaVenta = fechaVenta;
        this.precio = precio;
    }

    public Long getVentaId() { return ventaId; }
    public Long getBilleteId() { return billeteId; }
    public String getNumeroBillete() { return numeroBillete; }
    public Long getClienteId() { return clienteId; }
    public String getNombreCliente() { return nombreCliente; }
    public LocalDateTime getFechaVenta() { return fechaVenta; }
    public BigDecimal getPrecio() { return precio; }
}
