package org.konex.sistemaloteria.cliente.repository;

import org.konex.sistemaloteria.cliente.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio JPA para la entidad {@link Cliente}.
 *
 * <p>
 * Proporciona las operaciones CRUD básicas (crear, leer, actualizar, eliminar)
 * gracias a la herencia de {@link JpaRepository}, además de consultas
 * personalizadas específicas para la búsqueda de clientes por correo electrónico.
 * </p>
 *
 * <p>
 * Este componente forma parte del módulo de persistencia del
 * <b>Sistema de Ventas de Lotería</b> desarrollado para la prueba técnica de Konex Innovation:contentReference[oaicite:1]{index=1}.
 * </p>
 *
 * <h4>Consultas personalizadas incluidas:</h4>
 * <ul>
 *   <li>{@link #findByCorreo(String)} — busca un cliente por su correo electrónico.</li>
 *   <li>{@link #existsByCorreo(String)} — verifica si ya existe un cliente registrado con ese correo.</li>
 * </ul>
 *
 * <h4>Ejemplo de uso:</h4>
 * <pre>
 * Optional&lt;Cliente&gt; cliente = clienteRepository.findByCorreo("juan@example.com");
 * boolean existe = clienteRepository.existsByCorreo("ana@example.com");
 * </pre>
 */
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    /**
     * Busca un cliente por su correo electrónico.
     *
     * @param correo dirección de correo electrónico del cliente.
     * @return un {@link Optional} que contiene el cliente si existe.
     */
    Optional<Cliente> findByCorreo(String correo);

    /**
     * Verifica si ya existe un cliente registrado con el correo proporcionado.
     *
     * @param correo dirección de correo electrónico a verificar.
     * @return {@code true} si existe un cliente con ese correo; de lo contrario {@code false}.
     */
    boolean existsByCorreo(String correo);
}
