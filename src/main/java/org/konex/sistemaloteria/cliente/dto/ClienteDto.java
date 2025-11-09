package org.konex.sistemaloteria.cliente.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO que representa los datos de un cliente.
 *
 * <p>
 * Se utiliza en las operaciones CRUD del módulo de clientes:
 * <ul>
 *   <li>Registro de nuevos clientes.</li>
 *   <li>Listado y consulta individual de clientes.</li>
 *   <li>Actualización de información existente.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Incluye validaciones mediante anotaciones de {@link jakarta.validation}:
 * <ul>
 *   <li>{@link NotBlank} — asegura que los campos nombre y correo no estén vacíos.</li>
 *   <li>{@link Email} — valida el formato correcto del correo electrónico.</li>
 * </ul>
 * </p>
 *
 * <h4>Ejemplo de JSON esperado:</h4>
 * <pre>
 * {
 *   "id": 1,
 *   "nombre": "Ana Gómez",
 *   "correo": "ana.gomez@example.com"
 * }
 * </pre>
 *
 * <p>
 * Forma parte del modelo de transferencia de datos definido en la
 * <b>Prueba Técnica – Sistema de Ventas de Lotería</b> de Konex Innovation:contentReference[oaicite:1]{index=1}.
 * </p>
 */
@Data
public class ClienteDto {

    /** Identificador único del cliente (autogenerado por la base de datos). */
    private Long id;

    /** Nombre completo del cliente. No puede estar vacío. */
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    /** Correo electrónico del cliente. Debe ser válido y único en el sistema. */
    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo no tiene un formato válido")
    private String correo;
}
