package org.konex.sistemaloteria.billete.repository;

import org.konex.sistemaloteria.billete.model.Billete;
import org.konex.sistemaloteria.compartido.EstadoBillete;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BilleteRepository extends JpaRepository<Billete, Long> {
    List<Billete> findBySorteoId(Long sorteoId);
    List<Billete> findBySorteoIdAndEstado(Long sorteoId, EstadoBillete estado);

    // Historial: billetes por cliente
    List<Billete> findByClienteId(Long clienteId);
}
