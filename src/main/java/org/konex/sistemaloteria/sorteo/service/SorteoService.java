package org.konex.sistemaloteria.sorteo.service;

import java.util.List;
import org.konex.sistemaloteria.sorteo.dto.SorteoDto;

public interface SorteoService {
    SorteoDto crear(SorteoDto dto);
    List<SorteoDto> listar();
}
