package org.konex.sistemaloteria.venta.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de salida que representa la información resultante
 * de una venta registrada en el sistema.
 *
 * <p>
 * Se utiliza principalmente como respuesta del servicio de ventas
 * ({@code VentaService.vender}) para retornar al cliente del front-end
 * un resumen claro y seguro de la transacción realizada.
 * </p>
 *
 * <p>
 * Incluye datos del billete vendido, el cliente comprador,
 * el precio y la fecha de venta.
 * </p>
 *
 * Ejemplo de uso:
 * <pre>
 *     VentaResponseDto dto = ventaService.vender(request);
 *     System.out.println(dto.getNumeroBillete());
 * </pre>
 */
public class VentaResponseDto {

    /** Identificador único de la venta registrada. */
    private Long ventaId;

    /** Identificador del billete vendido. */
    private Long billeteId;

    /** Número impreso o código del billete vendido. */
    private String numeroBillete;

    /** Identificador del cliente que realizó la compra. */
    private Long clienteId;

    /** Nombre completo del cliente comprador. */
    private String nombreCliente;

    /** Fecha y hora exacta en la que se registró la venta. */
    private LocalDateTime fechaVenta;

    /** Precio de venta correspondiente al billete adquirido. */
    private BigDecimal precio;

    /**
     * Constructor completo para inicializar todos los campos del DTO.
     *
     * @param ventaId ID único de la venta.
     * @param billeteId ID del billete vendido.
     * @param numeroBillete número o código del billete.
     * @param clienteId ID del cliente comprador.
     * @param nombreCliente nombre del cliente comprador.
     * @param fechaVenta fecha y hora de la venta.
     * @param precio valor del billete vendido.
     */
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
