package org.konex.sistemaloteria.billete.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.konex.sistemaloteria.billete.dto.BilleteDto;
import org.konex.sistemaloteria.billete.model.Billete;
import org.konex.sistemaloteria.billete.repository.BilleteRepository;
import org.konex.sistemaloteria.compartido.EstadoBillete;
import org.konex.sistemaloteria.sorteo.model.Sorteo;
import org.konex.sistemaloteria.sorteo.repository.SorteoRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.ArgumentCaptor;

class BilleteServiceImplTest {

    private BilleteRepository billeteRepo;
    private SorteoRepository sorteoRepo;
    private BilleteServiceImpl service;

    @BeforeEach
    void setUp() {
        billeteRepo = mock(BilleteRepository.class);
        sorteoRepo = mock(SorteoRepository.class);
        service = new BilleteServiceImpl(billeteRepo, sorteoRepo);
    }

    @Test
    void crearBillete_ok_conEstadoPorDefectoDisponible() {
        // arrange
        Long sorteoId = 10L;
        Sorteo sorteo = new Sorteo();
        sorteo.setId(sorteoId);

        BilleteDto req = new BilleteDto();
        req.setNumero("0007");
        req.setPrecio(new BigDecimal("12000"));
        req.setSorteoId(sorteoId);
        // sin estado -> debe quedar DISPONIBLE

        when(sorteoRepo.findById(sorteoId)).thenReturn(Optional.of(sorteo));

        Billete guardado = Billete.builder()
                .id(123L)
                .numero("0007")
                .precio(new BigDecimal("12000"))
                .estado(EstadoBillete.DISPONIBLE)
                .sorteo(sorteo)
                .build();

        // capturamos el Billete que se intenta guardar
        ArgumentCaptor<Billete> captor = ArgumentCaptor.forClass(Billete.class);
        when(billeteRepo.save(any(Billete.class))).thenReturn(guardado);

        // act
        BilleteDto resp = service.crearBillete(req);

        // assert
        verify(sorteoRepo).findById(sorteoId);
        verify(billeteRepo).save(captor.capture());

        Billete enviado = captor.getValue();
        assertEquals("0007", enviado.getNumero());
        assertEquals(new BigDecimal("12000"), enviado.getPrecio());
        assertEquals(EstadoBillete.DISPONIBLE, enviado.getEstado());
        assertEquals(sorteoId, enviado.getSorteo().getId());

        assertEquals(123L, resp.getId());
        assertEquals(EstadoBillete.DISPONIBLE, resp.getEstado());
        assertEquals(sorteoId, resp.getSorteoId());
    }

    @Test
    void crearBillete_respetaEstadoCuandoSeEnvia() {
        Long sorteoId = 20L;
        Sorteo sorteo = new Sorteo();
        sorteo.setId(sorteoId);

        BilleteDto req = new BilleteDto();
        req.setNumero("0010");
        req.setPrecio(new BigDecimal("8000"));
        req.setSorteoId(sorteoId);
        req.setEstado(EstadoBillete.VENDIDO); // explícito

        when(sorteoRepo.findById(sorteoId)).thenReturn(Optional.of(sorteo));

        Billete guardado = Billete.builder()
                .id(200L)
                .numero("0010")
                .precio(new BigDecimal("8000"))
                .estado(EstadoBillete.VENDIDO)
                .sorteo(sorteo)
                .build();
        when(billeteRepo.save(any(Billete.class))).thenReturn(guardado);

        BilleteDto resp = service.crearBillete(req);

        assertEquals(200L, resp.getId());
        assertEquals(EstadoBillete.VENDIDO, resp.getEstado());
    }

    @Test
    void crearBillete_fallaCuandoNoExisteSorteo() {
        Long sorteoId = 99L;
        BilleteDto req = new BilleteDto();
        req.setNumero("0001");
        req.setPrecio(new BigDecimal("10000"));
        req.setSorteoId(sorteoId);

        when(sorteoRepo.findById(sorteoId)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.crearBillete(req));
        assertTrue(ex.getMessage().contains("no existe"));
        verify(billeteRepo, never()).save(any());
    }

    @Test
    void listarPorSorteo_ok_mapeaAListaDeDtos() {
        Long sorteoId = 42L;
        Sorteo s = new Sorteo();
        s.setId(sorteoId);

        Billete b1 = Billete.builder().id(1L).numero("0001").precio(new BigDecimal("10000"))
                .estado(EstadoBillete.DISPONIBLE).sorteo(s).build();
        Billete b2 = Billete.builder().id(2L).numero("0002").precio(new BigDecimal("10000"))
                .estado(EstadoBillete.VENDIDO).sorteo(s).build();

        when(billeteRepo.findBySorteoId(sorteoId)).thenReturn(List.of(b1, b2));

        var lista = service.listarPorSorteo(sorteoId);

        assertEquals(2, lista.size());
        assertEquals("0001", lista.get(0).getNumero());
        assertEquals(EstadoBillete.DISPONIBLE, lista.get(0).getEstado());
        assertEquals(sorteoId, lista.get(0).getSorteoId());

        assertEquals("0002", lista.get(1).getNumero());
        assertEquals(EstadoBillete.VENDIDO, lista.get(1).getEstado());
    }
}
