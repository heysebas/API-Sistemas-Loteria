package org.konex.sistemaloteria.venta.dto;

import jakarta.validation.constraints.NotNull;

public class VentaRequestDto {

    @NotNull(message = "El ID del billete es obligatorio")
    private Long billeteId;

    @NotNull(message = "El ID del cliente es obligatorio")
    private Long clienteId;

    public Long getBilleteId() { return billeteId; }
    public void setBilleteId(Long billeteId) { this.billeteId = billeteId; }
    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
}
