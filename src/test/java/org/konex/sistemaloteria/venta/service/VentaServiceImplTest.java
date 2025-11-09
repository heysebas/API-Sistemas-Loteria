package org.konex.sistemaloteria.venta.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.konex.sistemaloteria.billete.model.Billete;
import org.konex.sistemaloteria.billete.repository.BilleteRepository;
import org.konex.sistemaloteria.cliente.model.Cliente;
import org.konex.sistemaloteria.cliente.repository.ClienteRepository;
import org.konex.sistemaloteria.compartido.EstadoBillete;
import org.konex.sistemaloteria.venta.dto.VentaRequestDto;
import org.konex.sistemaloteria.venta.dto.VentaResponseDto;
import org.konex.sistemaloteria.venta.model.Venta;
import org.konex.sistemaloteria.venta.repository.VentaRepository;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para VentaServiceImpl con el contrato:
 * VentaRequestDto { billeteId, clienteId }.
 */
@ExtendWith(MockitoExtension.class)
class VentaServiceImplTest {

    @Mock private VentaRepository ventaRepo;
    @Mock private BilleteRepository billeteRepo;
    @Mock private ClienteRepository clienteRepo;

    private VentaServiceImpl service;

    @BeforeEach
    void setUp() {
        // Orden del constructor: BilleteRepository, ClienteRepository, VentaRepository
        service = new VentaServiceImpl(billeteRepo, clienteRepo, ventaRepo);
    }

    private Cliente cliente(Long id, String nombre) {
        var c = new Cliente();
        c.setId(id);
        c.setNombre(nombre);
        c.setCorreo(nombre.toLowerCase().replace(" ", ".") + "@example.com");
        return c;
    }

    private Billete billete(Long id, String numero, BigDecimal precio, EstadoBillete estado) {
        var b = new Billete();
        b.setId(id);
        b.setNumero(numero);
        b.setPrecio(precio);
        b.setEstado(estado);
        return b;
    }

    private VentaRequestDto req(long billeteId, long clienteId) {
        return new VentaRequestDto(billeteId, clienteId);
    }

    @Test
    @DisplayName("vender(): billete DISPONIBLE -> marca VENDIDO, guarda Venta y retorna DTO")
    void vender_ok() {
        var b = billete(10L, "0001", new BigDecimal("10000"), EstadoBillete.DISPONIBLE);
        var c = cliente(100L, "Cliente Prueba");

        when(billeteRepo.findById(10L)).thenReturn(Optional.of(b));
        when(clienteRepo.findById(100L)).thenReturn(Optional.of(c));
        when(ventaRepo.save(any(Venta.class))).thenAnswer(inv -> {
            var v = inv.getArgument(0, Venta.class);
            v.setId(999L);
            return v;
        });

        VentaResponseDto out = service.vender(req(10L, 100L));

        assertThat(out).isNotNull();
        assertThat(b.getEstado()).isEqualTo(EstadoBillete.VENDIDO);

        var cap = ArgumentCaptor.forClass(Venta.class);
        verify(ventaRepo).save(cap.capture());
        var guardada = cap.getValue();

        assertThat(guardada.getBillete().getId()).isEqualTo(10L);
        assertThat(guardada.getCliente().getId()).isEqualTo(100L);

        verify(billeteRepo).findById(10L);
        verify(clienteRepo).findById(100L);
    }

    @Test
    @DisplayName("vender(): billete VENDIDO -> lanza IllegalStateException (sin consultar cliente)")
    void vender_billeteVendido() {
        var b = billete(11L, "0002", new BigDecimal("10000"), EstadoBillete.VENDIDO);
        when(billeteRepo.findById(11L)).thenReturn(Optional.of(b));

        assertThrows(IllegalStateException.class, () ->
                service.vender(req(11L, 100L)));

        verifyNoInteractions(ventaRepo);
        verify(billeteRepo).findById(11L);
        verifyNoMoreInteractions(clienteRepo);
    }

    @Test
    @DisplayName("vender(): billete no existe -> lanza IllegalArgumentException")
    void vender_billeteNoExiste() {
        when(billeteRepo.findById(123L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                service.vender(req(123L, 100L)));

        verify(ventaRepo, never()).save(any());
    }

    @Test
    @DisplayName("vender(): cliente no existe -> lanza IllegalArgumentException y no muta estado")
    void vender_clienteNoExiste() {
        var b = billete(12L, "0003", new BigDecimal("10000"), EstadoBillete.DISPONIBLE);
        when(billeteRepo.findById(12L)).thenReturn(Optional.of(b));
        when(clienteRepo.findById(9999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                service.vender(req(12L, 9999L)));

        assertThat(b.getEstado()).isEqualTo(EstadoBillete.DISPONIBLE);
        verify(ventaRepo, never()).save(any());
    }

    @Test
    @DisplayName("Si falla al guardar la venta, el billete debe permanecer DISPONIBLE (no muta estado)")
    void vender_fallaPersistencia_noMutaEstado() {
        var b = billete(20L, "0010", new BigDecimal("12000"), EstadoBillete.DISPONIBLE);
        var c = cliente(200L, "Cliente X");

        when(billeteRepo.findById(20L)).thenReturn(Optional.of(b));
        when(clienteRepo.findById(200L)).thenReturn(Optional.of(c));
        when(ventaRepo.save(any(Venta.class))).thenThrow(new RuntimeException("Fallo DB"));

        assertThrows(RuntimeException.class, () -> service.vender(req(20L, 200L)));
        assertThat(b.getEstado()).isEqualTo(EstadoBillete.DISPONIBLE);
        verify(ventaRepo).save(any(Venta.class));
    }
}
