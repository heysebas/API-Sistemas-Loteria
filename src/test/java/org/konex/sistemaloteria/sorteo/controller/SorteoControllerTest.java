package org.konex.sistemaloteria.sorteo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.konex.sistemaloteria.excepciones.GlobalExceptionHandler;
import org.konex.sistemaloteria.sorteo.dto.SorteoDto;
import org.konex.sistemaloteria.sorteo.service.SorteoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas de capa web para SorteoController.
 */
@WebMvcTest(controllers = SorteoController.class)
@AutoConfigureMockMvc(addFilters = false) // ✅ desactiva filtros de Spring Security en pruebas
@Import(GlobalExceptionHandler.class)     // tu handler para 400/422/etc.
class SorteoControllerTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper om;

    @MockitoBean
    private SorteoService service; // único colaborador real del controller

    // mocks defensivos SOLO si tu controller inyecta estos beans (si no, bórralos):
    @MockitoBean
    private org.modelmapper.ModelMapper modelMapper;
    @MockitoBean
    private org.konex.sistemaloteria.billete.repository.BilleteRepository billeteRepository;

    private SorteoDto dto(Long id, String nombre, LocalDate fecha) {
        var d = new SorteoDto();
        d.setId(id);
        d.setNombre(nombre);
        d.setFechaSorteo(fecha);
        return d;
    }

    @Test
    @DisplayName("POST /api/sorteos crea sorteo y devuelve 201")
    void post_creaSorteo_201() throws Exception {
        var req = dto(null, "Sorteo de Navidad", LocalDate.of(2025, 12, 24));
        var res = dto(10L, "Sorteo de Navidad", LocalDate.of(2025, 12, 24));
        when(service.crear(any(SorteoDto.class))).thenReturn(res);

        mvc.perform(post("/api/sorteos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(10)))
                .andExpect(jsonPath("$.nombre", is("Sorteo de Navidad")))
                .andExpect(jsonPath("$.fechaSorteo", is("2025-12-24")));
    }

    @Test
    @DisplayName("GET /api/sorteos lista todos (200)")
    void get_listar_200() throws Exception {
        var a = dto(1L, "Año Nuevo", LocalDate.of(2025, 1, 1));
        var b = dto(2L, "San Valentín", LocalDate.of(2025, 2, 14));
        when(service.listar()).thenReturn(List.of(a, b));

        mvc.perform(get("/api/sorteos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nombre", is("Año Nuevo")))
                .andExpect(jsonPath("$[1].fechaSorteo", is("2025-02-14")));
    }

    @Test
    @DisplayName("POST /api/sorteos rechaza nombre vacío (400)")
    void post_rechazaNombreVacio_400() throws Exception {
        // requiere @NotBlank en nombre y @NotNull en fechaSorteo en SorteoDto
        var req = dto(null, "   ", LocalDate.of(2025, 6, 1));

        mvc.perform(post("/api/sorteos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/sorteos rechaza fecha nula (400)")
    void post_rechazaFechaNula_400() throws Exception {
        var req = dto(null, "Sorteo Inválido", null);

        mvc.perform(post("/api/sorteos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }
}
