package org.konex.sistemaloteria.venta.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.konex.sistemaloteria.excepciones.GlobalExceptionHandler;
import org.konex.sistemaloteria.venta.dto.VentaRequestDto;
import org.konex.sistemaloteria.venta.dto.VentaResponseDto;
import org.konex.sistemaloteria.venta.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

// 👇 nuevo import (reemplaza a org.springframework.boot.test.mock.mockito.MockBean)
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = VentaController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class VentaControllerTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper om;

    // 👇 reemplazo: antes @MockBean
    @MockitoBean
    private VentaService ventaService;

    private VentaResponseDto sampleResponse() {
        return new VentaResponseDto(
                999L, 10L, "0001", 100L, "Cliente Prueba",
                LocalDateTime.now(), new BigDecimal("10000")
        );
    }

    @Test
    @DisplayName("POST /api/ventas -> 201 Created (camino feliz)")
    void post_vender_201() throws Exception {
        when(ventaService.vender(any(VentaRequestDto.class))).thenReturn(sampleResponse());
        var body = new VentaRequestDto(10L, 100L);

        mvc.perform(post("/api/ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(body)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("400 cuando body es {} (violaciones @NotNull/@Positive)")
    void post_vender_bodyVacio_400() throws Exception {
        mvc.perform(post("/api/ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("400 cuando tipos son inválidos (clienteId como string)")
    void post_vender_tiposInvalidos_400() throws Exception {
        var raw = """
          {"billeteId": 10, "clienteId": "abc"}
        """;
        mvc.perform(post("/api/ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(raw))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("400 cuando ids negativos violan @Positive")
    void post_vender_idsNegativos_400() throws Exception {
        var body = new VentaRequestDto(-5L, -1L);
        mvc.perform(post("/api/ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("404 cuando no existe el recurso (NoSuchElementException)")
    void post_vender_notFound_404() throws Exception {
        when(ventaService.vender(any(VentaRequestDto.class)))
                .thenThrow(new NoSuchElementException("Billete no encontrado"));

        var body = new VentaRequestDto(10L, 100L);

        mvc.perform(post("/api/ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(body)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message", containsString("Billete no encontrado")));
    }

    @Test
    @DisplayName("409 cuando el billete ya está vendido (IllegalStateException)")
    void post_vender_conflict_409() throws Exception {
        when(ventaService.vender(any(VentaRequestDto.class)))
                .thenThrow(new IllegalStateException("El billete ya fue vendido"));

        var body = new VentaRequestDto(10L, 100L);

        mvc.perform(post("/api/ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(body)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message", containsString("ya fue vendido")));
    }
}
