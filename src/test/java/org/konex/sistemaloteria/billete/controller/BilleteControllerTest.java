package org.konex.sistemaloteria.billete.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.konex.sistemaloteria.billete.dto.BilleteDto;
import org.konex.sistemaloteria.billete.service.BilleteService;
import org.konex.sistemaloteria.compartido.EstadoBillete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BilleteController.class)
@AutoConfigureMockMvc(addFilters = false)
class BilleteControllerTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper mapper;

    // Spring 6.2+ reemplazo de @MockBean
    @MockitoBean
    private BilleteService service;

    // ---------- Happy path ----------
    @Test
    void post_crearBillete_devuelve201Ycuerpo() throws Exception {
        BilleteDto req = BilleteDto.builder()
                .numero("000123")
                .precio(new BigDecimal("9000"))
                .sorteoId(5L)
                .build();

        BilleteDto resp = BilleteDto.builder()
                .id(77L)
                .numero("000123")
                .precio(new BigDecimal("9000"))
                .estado(EstadoBillete.DISPONIBLE)
                .sorteoId(5L)
                .build();

        when(service.crearBillete(any(BilleteDto.class))).thenReturn(resp);

        mvc.perform(post("/api/billetes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(77))
                .andExpect(jsonPath("$.numero").value("000123"))
                .andExpect(jsonPath("$.estado").value("DISPONIBLE"))
                .andExpect(jsonPath("$.sorteoId").value(5));
    }

    @Test
    void get_listarPorSorteo_devuelve200YLista() throws Exception {
        BilleteDto b1 = BilleteDto.builder()
                .id(1L).numero("000001").precio(new BigDecimal("10000"))
                .estado(EstadoBillete.DISPONIBLE).sorteoId(9L).build();

        BilleteDto b2 = BilleteDto.builder()
                .id(2L).numero("000002").precio(new BigDecimal("10000"))
                .estado(EstadoBillete.VENDIDO).sorteoId(9L).build();

        when(service.listarPorSorteo(9L)).thenReturn(List.of(b1, b2));

        mvc.perform(get("/api/billetes/sorteo/{sorteoId}", 9L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].numero").value("000001"))
                .andExpect(jsonPath("$[1].estado").value("VENDIDO"));
    }

    // ---------- Validación de entrada (400) ----------
    @Test
    void post_crearBillete_rechazaNumeroNoNumerico_400() throws Exception {
        BilleteDto req = BilleteDto.builder()
                .numero("12ABCD")                 // ❌ inválido: no solo dígitos
                .precio(new BigDecimal("5000"))
                .sorteoId(1L)
                .build();

        mvc.perform(post("/api/billetes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("numero")))
                .andExpect(content().string(containsString("1 y 6 dígitos")));

        // No debe llamar al servicio si falló la validación
        verifyNoInteractions(service);
    }

    @Test
    void post_crearBillete_rechazaNumeroMuyLargo_400() throws Exception {
        BilleteDto req = BilleteDto.builder()
                .numero("1234567")               // ❌ 7 dígitos (máx 6)
                .precio(new BigDecimal("5000"))
                .sorteoId(1L)
                .build();

        mvc.perform(post("/api/billetes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("numero")));

        verifyNoInteractions(service);
    }

    @Test
    void post_crearBillete_rechazaPrecioNoPositivo_400() throws Exception {
        BilleteDto req = BilleteDto.builder()
                .numero("123")
                .precio(new BigDecimal("0"))      // ❌ debe ser > 0
                .sorteoId(1L)
                .build();

        mvc.perform(post("/api/billetes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("precio")));

        verifyNoInteractions(service);
    }

    @Test
    void post_crearBillete_rechazaSorteoIdNulo_400() throws Exception {
        BilleteDto req = BilleteDto.builder()
                .numero("987654")                 // válido (1..6 dígitos)
                .precio(new BigDecimal("8000"))
                .sorteoId(null)                   // ❌ obligatorio
                .build();

        mvc.perform(post("/api/billetes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("sorteoId")));

        verifyNoInteractions(service);
    }

    // ---------- Regla de negocio (409) ----------
    @Test
    void post_crearBillete_conflictoNumeroDuplicado_409() throws Exception {
        BilleteDto req = BilleteDto.builder()
                .numero("123456")
                .precio(new BigDecimal("9000"))
                .sorteoId(2L)
                .build();

        // Simula que el servicio detecta duplicado en ese sorteo
        when(service.crearBillete(any(BilleteDto.class)))
                .thenThrow(new IllegalStateException("El número ya existe en el sorteo"));

        mvc.perform(post("/api/billetes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isConflict())
                .andExpect(content().string(containsString("ya existe")));

        verify(service, times(1)).crearBillete(any(BilleteDto.class));
    }
}
