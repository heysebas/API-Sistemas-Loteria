package org.konex.sistemaloteria.billete.service;

import java.util.List;
import org.konex.sistemaloteria.billete.dto.BilleteDto;

public interface BilleteService {
    BilleteDto crearBillete(BilleteDto dto);
    List<BilleteDto> listarPorSorteo(Long sorteoId);
}
