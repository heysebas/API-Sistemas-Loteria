package org.konex.sistemaloteria.sorteo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.konex.sistemaloteria.sorteo.model.Sorteo;

/**
 * Repositorio JPA para la entidad {@link Sorteo}.
 *
 * <p>
 * Proporciona las operaciones CRUD básicas (crear, leer, actualizar, eliminar)
 * gracias a la herencia de {@link JpaRepository}.
 * </p>
 *
 * <p>
 * Es responsable de la persistencia de los datos de los sorteos,
 * incluyendo nombre y fecha, dentro del sistema de ventas de lotería.
 * </p>
 *
 * <p>
 * Implementa automáticamente consultas estándar basadas en convención
 * de nombres de métodos y puede ser extendido con consultas personalizadas.
 * </p>
 *
 * Ejemplo de uso:
 * <pre>
 *     Sorteo nuevo = sorteoRepository.save(new Sorteo("Navidad", LocalDate.of(2025, 12, 24)));
 *     List&lt;Sorteo&gt; todos = sorteoRepository.findAll();
 * </pre>
 */
public interface SorteoRepository extends JpaRepository<Sorteo, Long> {
}
