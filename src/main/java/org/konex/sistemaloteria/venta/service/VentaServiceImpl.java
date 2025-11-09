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

/**
 * Servicio principal para gestionar el proceso de venta de billetes.
 *
 * Este servicio se encarga de:
 * <ul>
 *   <li>Validar la existencia del billete y del cliente.</li>
 *   <li>Verificar que el billete no haya sido vendido previamente.</li>
 *   <li>Actualizar el estado del billete a "VENDIDO" y asociarlo al cliente.</li>
 *   <li>Registrar una nueva venta con la fecha y precio correspondientes.</li>
 *   <li>Devolver un DTO con los datos de la venta realizada.</li>
 * </ul>
 *
 * La transacción se marca como @Transactional para asegurar la atomicidad:
 * si ocurre un error, se revierte el cambio en el billete y la venta.
 */
@Service
@RequiredArgsConstructor
public class VentaServiceImpl implements VentaService {

    /** Repositorio de billetes para acceder y actualizar su estado. */
    private final BilleteRepository billeteRepo;

    /** Repositorio de clientes para validar la existencia del comprador. */
    private final ClienteRepository clienteRepo;

    /** Repositorio de ventas para registrar las transacciones realizadas. */
    private final VentaRepository ventaRepo;

    /**
     * Procesa la venta de un billete a un cliente.
     *
     * @param request objeto {@link VentaRequestDto} con los IDs del billete y del cliente.
     * @return un {@link VentaResponseDto} con los datos de la venta confirmada.
     *
     * @throws IllegalArgumentException si el billete o el cliente no existen.
     * @throws IllegalStateException si el billete ya fue vendido previamente.
     */
    @Override
    @Transactional
    public VentaResponseDto vender(VentaRequestDto request) {
        // Buscar el billete solicitado
        Billete billete = billeteRepo.findById(request.getBilleteId())
                .orElseThrow(() -> new IllegalArgumentException("El billete no existe."));

        // Validar que el billete no haya sido vendido
        if (billete.getEstado() == EstadoBillete.VENDIDO) {
            throw new IllegalStateException("El billete ya fue vendido.");
        }

        // Buscar el cliente comprador
        Cliente cliente = clienteRepo.findById(request.getClienteId())
                .orElseThrow(() -> new IllegalArgumentException("El cliente no existe."));

        // Actualizar estado y asociar cliente al billete
        billete.setEstado(EstadoBillete.VENDIDO);
        billete.setCliente(cliente);
        billeteRepo.save(billete);

        // Crear y registrar la venta
        Venta venta = Venta.builder()
                .billete(billete)
                .cliente(cliente)
                .fechaVenta(LocalDateTime.now())
                .precio(billete.getPrecio())
                .build();

        Venta guardada = ventaRepo.save(venta);

        // Retornar un DTO con la información de la venta registrada
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
