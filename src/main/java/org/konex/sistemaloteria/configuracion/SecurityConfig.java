package org.konex.sistemaloteria.configuracion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.*;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Configuración central de seguridad para la aplicación.
 *
 * <p>
 * Esta clase define las políticas de seguridad HTTP, CORS y de sesión
 * para el backend del <b>Sistema de Ventas de Lotería</b> desarrollado
 * en el marco de la <b>Prueba Técnica – Konex Innovation</b>:contentReference[oaicite:1]{index=1}.
 * </p>
 *
 * <p>
 * Se utiliza {@link EnableWebSecurity} junto con {@link SecurityFilterChain}
 * para definir de forma explícita el comportamiento de acceso a los endpoints.
 * </p>
 *
 * <h3>Características principales:</h3>
 * <ul>
 *   <li>Permite acceso sin autenticación a los endpoints públicos (API REST y consola H2).</li>
 *   <li>Desactiva CSRF para permitir peticiones POST desde el frontend Angular.</li>
 *   <li>Configura CORS para permitir comunicación entre <code>localhost:8080</code> y <code>localhost:4200</code>.</li>
 *   <li>Define sesiones sin estado (STATELESS) ya que no se maneja autenticación persistente.</li>
 * </ul>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Define la cadena de filtros de seguridad (Security Filter Chain)
     * que gestiona las políticas de acceso y protección de los endpoints.
     *
     * @param http objeto {@link org.springframework.security.config.annotation.web.builders.HttpSecurity}
     *             proporcionado por el contenedor de Spring.
     * @return la configuración de seguridad final aplicada al contexto.
     * @throws Exception en caso de error durante la construcción de la cadena.
     */
    @Bean
    public SecurityFilterChain filterChain(org.springframework.security.config.annotation.web.builders.HttpSecurity http) throws Exception {
        return http
                // 🔹 Configuración CSRF: deshabilitada excepto para la consola H2
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**") // permite formularios del H2
                        .disable()
                )

                // 🔹 Permitir iframes desde el mismo origen (necesario para H2 Console)
                .headers(h -> h
                        .frameOptions(f -> f.sameOrigin())
                )

                // 🔹 No se gestionan sesiones (la API es stateless)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 🔹 Activar configuración CORS (permitir comunicación con Angular)
                .cors(Customizer.withDefaults())

                // 🔹 Configuración de acceso público
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll() // acceso total a consola H2
                        .requestMatchers(HttpMethod.GET, "/actuator/health").permitAll() // endpoint de salud
                        .anyRequest().permitAll() // resto de endpoints abiertos (sin autenticación)
                )

                // 🔹 Construir la configuración final
                .build();
    }

    /**
     * Configura la política CORS (Cross-Origin Resource Sharing)
     * para permitir que el frontend (Angular) interactúe con el backend sin restricciones.
     *
     * <p>
     * En este entorno de desarrollo:
     * <ul>
     *   <li>Frontend: <code>http://localhost:4200</code></li>
     *   <li>Backend: <code>http://localhost:8080</code></li>
     * </ul>
     * </p>
     *
     * <p>
     * Se permiten todos los métodos HTTP comunes (GET, POST, PUT, DELETE, OPTIONS)
     * y todas las cabeceras.
     * </p>
     *
     * @return la fuente de configuración CORS registrada.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowCredentials(true);
        cfg.setAllowedOriginPatterns(List.of("http://localhost:4200")); // origen permitido (Angular)
        cfg.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
        cfg.setAllowedHeaders(List.of("*"));
        cfg.setExposedHeaders(List.of("Location")); // cabeceras visibles en la respuesta

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }
}
