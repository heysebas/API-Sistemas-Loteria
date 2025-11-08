package org.konex.sistemaloteria.venta.service;

import org.konex.sistemaloteria.venta.dto.VentaRequestDto;
import org.konex.sistemaloteria.venta.dto.VentaResponseDto;

public interface VentaService {
    VentaResponseDto vender(VentaRequestDto request);
}
