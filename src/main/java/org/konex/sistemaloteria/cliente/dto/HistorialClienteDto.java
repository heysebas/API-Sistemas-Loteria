package org.konex.sistemaloteria.cliente.dto;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * DTO que representa el historial completo de un cliente,
 * incluyendo sus datos básicos y los billetes asociados a sus compras.
 *
 * <p>
 * Se utiliza en el módulo de clientes para devolver una respuesta consolidada
 * al consultar el historial por correo electrónico.
 * </p>
 *
 * <p>
 * Este objeto combina información proveniente de las entidades:
 * <ul>
 *   <li>{@code Cliente}</li>
 *   <li>{@code Venta}</li>
 *   <li>{@code Billete}</li>
 *   <li>{@code Sorteo}</li>
 * </ul>
 * </p>
 *
 * <p>
 * Forma parte del diseño exigido en la <b>Prueba Técnica – Sistema de Ventas de Lotería</b>
 * de Konex Innovation:contentReference[oaicite:1]{index=1}.
 * </p>
 *
 * <h4>Ejemplo de respuesta JSON:</h4>
 * <pre>
 * {
 *   "id": 1,
 *   "nombre": "Juan Pérez",
 *   "correo": "juan@example.com",
 *   "billetes": [
 *     {
 *       "id": 15,
 *       "numero": "0007",
 *       "precio": 10000,
 *       "estado": "VENDIDO",
 *       "sorteoId": 3,
 *       "sorteoNombre": "Sorteo de Navidad"
 *     },
 *     {
 *       "id": 28,
 *       "numero": "0012",
 *       "precio": 8000,
 *       "estado": "VENDIDO",
 *       "sorteoId": 4,
 *       "sorteoNombre": "Sorteo de Verano"
 *     }
 *   ]
 * }
 * </pre>
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HistorialClienteDto {

    /** Identificador único del cliente. */
    private Long id;

    /** Nombre completo del cliente. */
    private String nombre;

    /** Correo electrónico del cliente. */
    private String correo;

    /** Lista de billetes asociados a las ventas del cliente. */
    private List<BilleteResumen> billetes;

    /**
     * Subclase estática que representa un resumen de un billete
     * dentro del historial de un cliente.
     *
     * <p>
     * Incluye los datos esenciales del billete y su sorteo asociado.
     * </p>
     */
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BilleteResumen {

        /** Identificador único del billete. */
        private Long id;

        /** Número o código del billete (ej: 0001, 0010, etc.). */
        private String numero;

        /** Precio de venta del billete. */
        private BigDecimal precio;

        /** Estado actual del billete (DISPONIBLE o VENDIDO). */
        private String estado;

        /** Identificador del sorteo al que pertenece el billete. */
        private Long sorteoId;

        /** Nombre descriptivo del sorteo asociado al billete. */
        private String sorteoNombre;
    }
}
