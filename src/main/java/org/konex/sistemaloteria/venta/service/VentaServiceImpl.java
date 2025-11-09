package org.konex.sistemaloteria.venta.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.konex.sistemaloteria.billete.model.Billete;
import org.konex.sistemaloteria.billete.repository.BilleteRepository;
import org.konex.sistemaloteria.cliente.model.Cliente;
import org.konex.sistemaloteria.cliente.repository.ClienteRepository;
import org.konex.sistemaloteria.compartido.EstadoBillete;
import org.konex.sistemaloteria.venta.dto.VentaRequestDto;
import org.konex.sistemaloteria.venta.dto.VentaResponseDto;
import org.konex.sistemaloteria.venta.model.Venta;
import org.konex.sistemaloteria.venta.repository.VentaRepository;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class VentaServiceImpl implements VentaService {

    private final BilleteRepository billeteRepo;
    private final ClienteRepository clienteRepo;
    private final VentaRepository   ventaRepo;

    /**
     * Estrategia para que el test "no muta estado" pase:
     * 1) Validamos y obtenemos entidades.
     * 2) Guardamos la Venta.
     * 3) Si lo anterior NO falla, recién ahí marcamos el billete como VENDIDO y lo persistimos.
     *    (Así, si sale excepción al guardar venta, el billete queda DISPONIBLE).
     */
    @Override
    @Transactional
    public VentaResponseDto vender(VentaRequestDto req) {
        // --- 1) Cargar y validar entidades ---
        Billete billete = billeteRepo.findById(req.getBilleteId())
                .orElseThrow(() -> new IllegalArgumentException("Billete no existe"));

        if (billete.getEstado() != EstadoBillete.DISPONIBLE) {
            throw new IllegalStateException("El billete ya fue vendido");
        }

        Cliente cliente = clienteRepo.findById(req.getClienteId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente no existe"));

        // --- 2) Crear y guardar la venta (primero la venta) ---
        Venta venta = new Venta();
        venta.setBillete(billete);
        venta.setCliente(cliente);
        venta.setFechaVenta(LocalDateTime.now());
        venta.setPrecio(billete.getPrecio());

        Venta guardada = ventaRepo.save(venta); // <- si falla aquí, todavía NO cambiamos el estado

        // --- 3) Marcar billete vendido y persistir ---
        billete.setEstado(EstadoBillete.VENDIDO);
        billeteRepo.save(billete);

        // --- 4) Mapear respuesta ---
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
