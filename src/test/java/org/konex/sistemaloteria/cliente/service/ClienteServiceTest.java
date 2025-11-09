package org.konex.sistemaloteria.cliente.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.konex.sistemaloteria.billete.model.Billete;
import org.konex.sistemaloteria.cliente.dto.ClienteDto;
import org.konex.sistemaloteria.cliente.dto.HistorialClienteDto;
import org.konex.sistemaloteria.cliente.model.Cliente;
import org.konex.sistemaloteria.cliente.repository.ClienteRepository;
import org.konex.sistemaloteria.compartido.EstadoBillete;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias de la capa de servicio ClienteServiceImpl.
 *
 * Verifica:
 *  - Creación de cliente con éxito
 *  - Rechazo de duplicados
 *  - Historial por correo correcto
 *  - Error si el correo no existe
 */
class ClienteServiceTest {

    private ClienteRepository repo;
    private ClienteServiceImpl service;

    @BeforeEach
    void init() {
        repo = mock(ClienteRepository.class);
        // Si tu ClienteServiceImpl tiene solo ClienteRepository en el constructor:
        service = new ClienteServiceImpl(repo);
        // Si tu implementación requiere también VentaRepository, usa:
        // service = new ClienteServiceImpl(repo, ventaRepoMock);
    }

    /* =====================================================
       CREAR CLIENTE
    ===================================================== */

    @Test
    void crearCliente_devuelveDtoConId() {
        ClienteDto dto = new ClienteDto();
        dto.setNombre("Juan Pérez");
        dto.setCorreo("juan@example.com");

        Cliente guardado = new Cliente();
        guardado.setId(1L);
        guardado.setNombre("Juan Pérez");
        guardado.setCorreo("juan@example.com");

        when(repo.save(any(Cliente.class))).thenReturn(guardado);

        ClienteDto result = service.crearCliente(dto);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getNombre()).isEqualTo("Juan Pérez");
        assertThat(result.getCorreo()).isEqualTo("juan@example.com");
        verify(repo, times(1)).save(any(Cliente.class));
    }

    @Test
    void crearCliente_rechazaDuplicadoPorCorreo() {
        Cliente existente = new Cliente();
        existente.setId(99L);
        existente.setCorreo("ana@example.com");

        when(repo.findByCorreo("ana@example.com")).thenReturn(Optional.of(existente));

        ClienteDto dto = new ClienteDto();
        dto.setNombre("Ana Gómez");
        dto.setCorreo("ana@example.com");

        assertThrows(IllegalArgumentException.class, () -> service.crearCliente(dto));
    }

    /* =====================================================
       HISTORIAL POR CORREO
    ===================================================== */

    @Test
    void historialPorCorreo_devuelveClienteYBilletes() {
        Cliente cliente = new Cliente();
        cliente.setId(10L);
        cliente.setNombre("Ana Gómez");
        cliente.setCorreo("ana@example.com");

        Billete b1 = new Billete();
        b1.setId(1L);
        b1.setNumero("0001");
        b1.setPrecio(new BigDecimal("10000"));
        b1.setEstado(EstadoBillete.VENDIDO);

        Billete b2 = new Billete();
        b2.setId(2L);
        b2.setNumero("0002");
        b2.setPrecio(new BigDecimal("12000"));
        b2.setEstado(EstadoBillete.DISPONIBLE);

        cliente.setBilletes(List.of(b1, b2));

        when(repo.findByCorreo("ana@example.com")).thenReturn(Optional.of(cliente));

        HistorialClienteDto result = service.historialPorCorreo("ana@example.com");

        // 👇 Campos del cliente están en la raíz del DTO
        assertThat(result.getId()).isEqualTo(10L);
        assertThat(result.getNombre()).isEqualTo("Ana Gómez");
        assertThat(result.getCorreo()).isEqualTo("ana@example.com");

        // 👇 Billetes: 'estado' es String, no enum
        assertThat(result.getBilletes()).hasSize(2);
        assertThat(result.getBilletes().get(0).getNumero()).isEqualTo("0001");
        assertThat(result.getBilletes().get(1).getEstado()).isEqualTo("DISPONIBLE");
    }

    @Test
    void historialPorCorreo_lanzaErrorSiNoExiste() {
        when(repo.findByCorreo("noexiste@example.com")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                service.historialPorCorreo("noexiste@example.com"));
    }
}
