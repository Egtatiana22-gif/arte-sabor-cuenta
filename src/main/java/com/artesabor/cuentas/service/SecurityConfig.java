
package com.artesabor.cuentas.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain configurarSeguridad(HttpSecurity http)
            throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((request, response, error) -> {
                    response.sendError(
                        HttpServletResponse.SC_UNAUTHORIZED,
                        "Debes autenticarte para acceder a este recurso."
                    );
                })
            )
            .authorizeHttpRequests(auth -> auth
                // Recursos públicos de la página.
                .requestMatchers(
                    "/",
                    "/index.html",
                    "/index.js",
                    "/estilos.css",
                    "/static/**",
                    "/Imagenes/**",
                    "/favicon.ico",
                    "/arte-sabor-paneles/**"
                ).permitAll()

                // Consultar el estado del servicio no requiere autenticación.
                .requestMatchers(HttpMethod.GET, "/api/estado").permitAll()

                // Solo el registro y el inicio de sesión son públicos.
                .requestMatchers(
                    HttpMethod.POST,
                    "/api/auth/registro",
                    "/api/auth/login"
                ).permitAll()
                // Solo el administrador puede gestionar las cuentas de usuarios.
                .requestMatchers("/api/admin/**")
                    .hasRole("ADMINISTRADOR")
                // Los tres roles pueden consultar su perfil.
                .requestMatchers("/api/perfil/**")
                    .hasAnyRole("CLIENTE", "EMPLEADO", "ADMINISTRADOR")

                // Las demás rutas requieren autenticación.
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .httpBasic(basic -> basic.disable())
            .formLogin(form -> form.disable())
            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}
