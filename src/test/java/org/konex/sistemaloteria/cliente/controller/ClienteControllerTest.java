package org.konex.sistemaloteria.cliente.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.konex.sistemaloteria.billete.dto.BilleteDto;
import org.konex.sistemaloteria.billete.repository.BilleteRepository;
import org.konex.sistemaloteria.cliente.dto.ClienteDto;
import org.konex.sistemaloteria.cliente.dto.HistorialClienteDto;
import org.konex.sistemaloteria.cliente.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
@AutoConfigureMockMvc(addFilters = false)
class ClienteControllerTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper mapper;

    @MockitoBean private ClienteService service;          // <-- mock del service
    @MockitoBean private BilleteRepository billeteRepo;   // <-- mock del repo usado por el controller

    /* ===================== CREATE ===================== */

    @Test
    void post_crearCliente_devuelve201() throws Exception {
        ClienteDto req = new ClienteDto();
        req.setNombre("Ana Gómez");
        req.setCorreo("ana@example.com");

        ClienteDto resp = new ClienteDto();
        resp.setId(10L);
        resp.setNombre("Ana Gómez");
        resp.setCorreo("ana@example.com");

        when(service.crearCliente(any(ClienteDto.class))).thenReturn(resp);

        mvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.nombre").value("Ana Gómez"))
                .andExpect(jsonPath("$.correo").value("ana@example.com"));
    }

    @Test
    void post_crearCliente_rechazaNombreVacio_400() throws Exception {
        ClienteDto req = new ClienteDto();
        req.setNombre("  "); // inválido
        req.setCorreo("ana@example.com");

        mvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void post_crearCliente_rechazaCorreoInvalido_400() throws Exception {
        ClienteDto req = new ClienteDto();
        req.setNombre("Ana");
        req.setCorreo("no-es-email"); // inválido

        mvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    /* ===================== HISTORIAL ===================== */

    @Test
    void get_historialPorCorreo_devuelve200YEstructura() throws Exception {
        String correo = "ana@example.com";

        // cliente “plano” en el historial (según tu DTO actual)
        // Tu HistorialClienteDto NO tiene objeto cliente anidado, sino campos id/nombre/correo y la lista de billetes
        HistorialClienteDto.BilleteResumen r1 = new HistorialClienteDto.BilleteResumen();
        r1.setId(1L);
        r1.setNumero("0001");
        r1.setPrecio(new BigDecimal("10000"));
        r1.setEstado("VENDIDO");
        r1.setSorteoId(3L);

        HistorialClienteDto.BilleteResumen r2 = new HistorialClienteDto.BilleteResumen();
        r2.setId(2L);
        r2.setNumero("0002");
        r2.setPrecio(new BigDecimal("8000"));
        r2.setEstado("DISPONIBLE");
        r2.setSorteoId(3L);

        HistorialClienteDto historial = HistorialClienteDto.builder()
                .id(10L)
                .nombre("Ana Gómez")
                .correo(correo)
                .billetes(List.of(r1, r2))
                .build();

        when(service.historialPorCorreo(eq(correo))).thenReturn(historial);

        mvc.perform(get("/api/clientes/historial").param("correo", correo))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.correo").value(correo))
                .andExpect(jsonPath("$.billetes.length()").value(2))
                .andExpect(jsonPath("$.billetes[0].numero").value("0001"))
                .andExpect(jsonPath("$.billetes[1].estado").value("DISPONIBLE"));
    }

    @Test
    void get_historial_rechazaCorreoInvalido_400() throws Exception {
        mvc.perform(get("/api/clientes/historial").param("correo", "mal-correo"))
                .andExpect(status().isBadRequest());
    }
}
