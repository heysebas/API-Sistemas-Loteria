package org.konex.sistemaloteria.sorteo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.konex.sistemaloteria.billete.repository.BilleteRepository;
import org.konex.sistemaloteria.sorteo.dto.SorteoDto;
import org.konex.sistemaloteria.sorteo.model.Sorteo;
import org.konex.sistemaloteria.sorteo.repository.SorteoRepository;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias de la lógica de SorteoServiceImpl (sin Spring context).
 */
class SorteoServiceImplTest {

    private SorteoRepository sorteoRepo;
    private BilleteRepository billeteRepo;
    private ModelMapper mapper;
    private SorteoService service;

    @BeforeEach
    void setUp() {
        sorteoRepo = mock(SorteoRepository.class);
        billeteRepo = mock(BilleteRepository.class);
        mapper = new ModelMapper();
        // ✅ constructor actualizado: (SorteoRepository, BilleteRepository, ModelMapper)
        service = new SorteoServiceImpl(sorteoRepo, billeteRepo, mapper);
    }

    private Sorteo entity(Long id, String nombre, LocalDate fecha) {
        return Sorteo.builder()
                .id(id)
                .nombre(nombre)
                .fechaSorteo(fecha)
                .build();
    }

    private SorteoDto dto(Long id, String nombre, LocalDate fecha) {
        var d = new SorteoDto();
        d.setId(id);
        d.setNombre(nombre);
        d.setFechaSorteo(fecha);
        return d;
    }

    @Test
    @DisplayName("crear(): persiste y devuelve DTO con ID")
    void crear_persisteYDevuelveDto() {
        var req = dto(null, "Día de la Madre 2025", LocalDate.of(2025, 5, 11));
        var saved = entity(100L, "Día de la Madre 2025", LocalDate.of(2025, 5, 11));

        when(sorteoRepo.save(any(Sorteo.class))).thenReturn(saved);

        var out = service.crear(req);

        assertThat(out.getId()).isEqualTo(100L);
        assertThat(out.getNombre()).isEqualTo("Día de la Madre 2025");
        assertThat(out.getFechaSorteo()).isEqualTo(LocalDate.of(2025, 5, 11));

        verify(sorteoRepo).save(any(Sorteo.class));
        verifyNoInteractions(billeteRepo); // en crear() no se toca billetes
    }

    @Test
    @DisplayName("listar(): devuelve lista mapeada")
    void listar_devuelveLista() {
        var e1 = entity(1L, "Año Nuevo", LocalDate.of(2025, 1, 1));
        var e2 = entity(2L, "Halloween", LocalDate.of(2025, 10, 31));
        when(sorteoRepo.findAll()).thenReturn(List.of(e1, e2));

        var list = service.listar();

        assertThat(list).hasSize(2);
        assertThat(list.get(0).getNombre()).isEqualTo("Año Nuevo");
        assertThat(list.get(1).getFechaSorteo()).isEqualTo(LocalDate.of(2025, 10, 31));

        verify(sorteoRepo).findAll();
        verifyNoInteractions(billeteRepo);
    }

}
