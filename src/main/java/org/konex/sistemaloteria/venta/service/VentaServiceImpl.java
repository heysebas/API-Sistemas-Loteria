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

/**
 * Servicio encargado de gestionar el proceso de venta de billetes.
 *
 * <p>
 * Estrategia implementada:
 * 1️⃣ Se validan y cargan las entidades (billete y cliente).<br>
 * 2️⃣ Se crea la venta y se guarda en la base de datos.<br>
 * 3️⃣ Si la venta se guarda correctamente, se marca el billete como VENDIDO
 *     y se le asigna el cliente correspondiente.<br>
 * 4️⃣ Se retorna una respuesta detallada con toda la información de la venta.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class VentaServiceImpl implements VentaService {

    private final BilleteRepository billeteRepo;
    private final ClienteRepository clienteRepo;
    private final VentaRepository ventaRepo;

    /**
     * Registra una nueva venta de billete.
     *
     * @param req datos de la venta (sorteoId, billeteId, clienteId)
     * @return DTO con los datos resultantes de la operación
     */
    @Override
    @Transactional
    public VentaResponseDto vender(VentaRequestDto req) {

        // --- 1️⃣ Validar y obtener entidades ---
        Billete billete = billeteRepo.findById(req.getBilleteId())
                .orElseThrow(() -> new IllegalArgumentException("Billete no existe con ID: " + req.getBilleteId()));

        if (billete.getEstado() != EstadoBillete.DISPONIBLE) {
            throw new IllegalStateException("El billete ya fue vendido o no está disponible");
        }

        Cliente cliente = clienteRepo.findById(req.getClienteId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente no existe con ID: " + req.getClienteId()));

        // --- 2️⃣ Crear la venta y guardarla ---
        Venta venta = new Venta();
        venta.setBillete(billete);
        venta.setCliente(cliente);
        venta.setFechaVenta(LocalDateTime.now());
        venta.setPrecio(billete.getPrecio());

        Venta guardada = ventaRepo.save(venta);

        // --- 3️⃣ Marcar billete como vendido y asignar cliente ---
        billete.setCliente(cliente);
        billete.setEstado(EstadoBillete.VENDIDO);
        billeteRepo.save(billete);

        // --- 4️⃣ Mapear respuesta ---
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
