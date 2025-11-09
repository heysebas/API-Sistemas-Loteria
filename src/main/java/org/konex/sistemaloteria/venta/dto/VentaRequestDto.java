package org.konex.sistemaloteria.venta.dto;

import jakarta.validation.constraints.NotNull;

/**
 * DTO de entrada para procesar una venta de billete.
 *
 * <p>
 * Este objeto se recibe desde el cliente (front-end o API)
 * y contiene los identificadores necesarios para registrar la venta:
 * <ul>
 *   <li>El ID del billete que se desea comprar.</li>
 *   <li>El ID del cliente que realiza la compra.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Se aplican validaciones con {@link NotNull} para asegurar que
 * ambos campos sean obligatorios antes de ejecutar la lógica del servicio.
 * </p>
 *
 * Ejemplo JSON esperado:
 * <pre>
 * {
 *   "billeteId": 12,
 *   "clienteId": 5
 * }
 * </pre>
 */
public class VentaRequestDto {

    /** Identificador del billete a vender. No puede ser nulo. */
    @NotNull(message = "El ID del billete es obligatorio")
    private Long billeteId;

    /** Identificador del cliente comprador. No puede ser nulo. */
    @NotNull(message = "El ID del cliente es obligatorio")
    private Long clienteId;

    public Long getBilleteId() { return billeteId; }
    public void setBilleteId(Long billeteId) { this.billeteId = billeteId; }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
}
