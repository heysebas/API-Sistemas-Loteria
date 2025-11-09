package org.konex.sistemaloteria.compartido;

/**
 * Enumeración que define los posibles estados de un billete
 * dentro del sistema de ventas de lotería.
 *
 * <p>
 * Esta enumeración se utiliza en la entidad {@link org.konex.sistemaloteria.billete.model.Billete}
 * para representar de forma clara y tipada el estado actual de cada billete.
 * </p>
 *
 * <p>
 * Forma parte del paquete <b>compartido</b>, ya que su uso es transversal
 * a varios módulos del sistema (sorteos, ventas, consultas, etc.).
 * </p>
 *
 * <h3>Valores posibles:</h3>
 * <ul>
 *   <li><b>DISPONIBLE</b>: el billete está libre y puede ser vendido.</li>
 *   <li><b>VENDIDO</b>: el billete ya fue asignado a un cliente.</li>
 * </ul>
 *
 * <p>
 * Ejemplo de uso:
 * <pre>
 * billete.setEstado(EstadoBillete.DISPONIBLE);
 * if (billete.getEstado() == EstadoBillete.VENDIDO) {
 *     throw new IllegalStateException("El billete ya fue vendido");
 * }
 * </pre>
 * </p>
 */
public enum EstadoBillete {
    /** Billete disponible para la venta. */
    DISPONIBLE,

    /** Billete ya vendido a un cliente. */
    VENDIDO
}
