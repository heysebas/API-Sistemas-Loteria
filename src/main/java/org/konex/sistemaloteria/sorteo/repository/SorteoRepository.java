package org.konex.sistemaloteria.sorteo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.konex.sistemaloteria.sorteo.model.Sorteo;

public interface SorteoRepository extends JpaRepository<Sorteo, Long> {
}
