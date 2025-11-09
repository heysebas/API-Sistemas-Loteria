package org.konex.sistemaloteria.venta.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * DTO de entrada para procesar una venta de billete.
 *
 * Este objeto se recibe desde el cliente (front-end o API)
 * y contiene los identificadores necesarios para registrar la venta:
 *  - billeteId: ID del billete que se desea comprar.
 *  - clienteId: ID del cliente que realiza la compra.
 *
 * Validaciones:
 *  - @NotNull: ambos campos son obligatorios.
 *  - @Positive: ambos IDs deben ser > 0.
 *
 * Ejemplo JSON:
 * {
 *   "billeteId": 12,
 *   "clienteId": 5
 * }
 */
public class VentaRequestDto {

    /** Identificador del billete a vender. No puede ser nulo y debe ser positivo. */
    @NotNull(message = "El ID del billete es obligatorio")
    @Positive(message = "billeteId debe ser positivo")
    private Long billeteId;

    /** Identificador del cliente comprador. No puede ser nulo y debe ser positivo. */
    @NotNull(message = "El ID del cliente es obligatorio")
    @Positive(message = "clienteId debe ser positivo")
    private Long clienteId;

    public VentaRequestDto() { }

    public VentaRequestDto(Long billeteId, Long clienteId) {
        this.billeteId = billeteId;
        this.clienteId = clienteId;
    }

    public Long getBilleteId() { return billeteId; }
    public void setBilleteId(Long billeteId) { this.billeteId = billeteId; }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    @Override
    public String toString() {
        return "VentaRequestDto{billeteId=" + billeteId + ", clienteId=" + clienteId + '}';
    }
}
