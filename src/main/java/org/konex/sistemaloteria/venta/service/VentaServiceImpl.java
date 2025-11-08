package org.konex.sistemaloteria.venta.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.konex.sistemaloteria.venta.dto.VentaRequestDto;
import org.konex.sistemaloteria.venta.dto.VentaResponseDto;
import org.konex.sistemaloteria.venta.model.Venta;
import org.konex.sistemaloteria.venta.repository.VentaRepository;
import org.konex.sistemaloteria.billete.repository.BilleteRepository;
import org.konex.sistemaloteria.billete.model.Billete;
import org.konex.sistemaloteria.compartido.EstadoBillete;
import org.konex.sistemaloteria.cliente.model.Cliente;
import org.konex.sistemaloteria.cliente.repository.ClienteRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VentaServiceImpl implements VentaService {

    private final BilleteRepository billeteRepo;
    private final ClienteRepository clienteRepo;
    private final VentaRepository ventaRepo;

    @Override
    @Transactional
    public VentaResponseDto vender(VentaRequestDto request) {
        Billete billete = billeteRepo.findById(request.getBilleteId())
                .orElseThrow(() -> new IllegalArgumentException("El billete no existe."));

        if (billete.getEstado() == EstadoBillete.VENDIDO) {
            throw new IllegalStateException("El billete ya fue vendido.");
        }

        Cliente cliente = clienteRepo.findById(request.getClienteId())
                .orElseThrow(() -> new IllegalArgumentException("El cliente no existe."));

        // Marcar billete como vendido y asociar cliente
        billete.setEstado(EstadoBillete.VENDIDO);
        billete.setCliente(cliente);
        billeteRepo.save(billete);

        // Registrar la venta
        Venta venta = Venta.builder()
                .billete(billete)
                .cliente(cliente)
                .fechaVenta(LocalDateTime.now())
                .precio(billete.getPrecio())
                .build();

        Venta guardada = ventaRepo.save(venta);

        return new VentaResponseDto(
                guardada.getId(),
                billete.getId(),
                billete.getNumero(),
                cliente.getId(),
                cliente.getNombre(),
                guardada.getFechaVenta(),
                guardada.getPrecio()
        );
    }
}
