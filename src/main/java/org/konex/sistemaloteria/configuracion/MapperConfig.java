package org.konex.sistemaloteria.configuracion;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración central para el mapeo de entidades y DTOs.
 *
 * <p>
 * Define un bean de {@link ModelMapper}, una herramienta que facilita
 * la conversión automática entre entidades del dominio y objetos de transferencia
 * de datos (DTOs), evitando la escritura manual de código repetitivo.
 * </p>
 *
 * <p>
 * Este componente es utilizado, por ejemplo, en el módulo de sorteos y ventas
 * para convertir instancias de:
 * <ul>
 *   <li>{@code Sorteo ↔ SorteoDto}</li>
 *   <li>{@code Venta ↔ VentaResponseDto}</li>
 * </ul>
 * </p>
 *
 * <p>
 * Forma parte de la configuración base del backend definida en la
 * <b>Prueba Técnica – Sistema de Ventas de Lotería</b> de Konex Innovation:contentReference[oaicite:1]{index=1}.
 * </p>
 *
 * <h4>Ejemplo de uso en un servicio:</h4>
 * <pre>
 * Sorteo sorteo = mapper.map(dto, Sorteo.class);
 * SorteoDto dto = mapper.map(sorteo, SorteoDto.class);
 * </pre>
 */
@Configuration
public class MapperConfig {

    /**
     * Registra una instancia única (singleton) de {@link ModelMapper}
     * en el contenedor de Spring para ser inyectada donde se necesite.
     *
     * @return instancia configurada de {@link ModelMapper}.
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
