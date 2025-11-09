package org.konex.sistemaloteria.venta.service;

import org.konex.sistemaloteria.venta.dto.VentaRequestDto;
import org.konex.sistemaloteria.venta.dto.VentaResponseDto;

/**
 * Interfaz que define el contrato del servicio de ventas.
 *
 * Su responsabilidad principal es procesar la compra de billetes,
 * garantizando la validación de datos y el registro de la transacción.
 *
 * Implementaciones típicas deben encargarse de:
 * <ul>
 *   <li>Verificar que el billete y el cliente existan en el sistema.</li>
 *   <li>Comprobar que el billete esté disponible antes de venderlo.</li>
 *   <li>Registrar la venta con la fecha, precio y referencias adecuadas.</li>
 *   <li>Devolver un {@link VentaResponseDto} con la información resultante.</li>
 * </ul>
 *
 * Ejemplo de implementación: {@link org.konex.sistemaloteria.venta.service.VentaServiceImpl}
 */
public interface VentaService {

    /**
     * Registra una venta de billete asociada a un cliente.
     *
     * @param request datos de entrada que incluyen los identificadores del billete y del cliente.
     * @return un objeto {@link VentaResponseDto} con el resumen de la venta.
     */
    VentaResponseDto vender(VentaRequestDto request);
}
