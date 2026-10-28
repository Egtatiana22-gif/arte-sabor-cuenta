package com.artesabor.cuentas.service;

import com.artesabor.cuentas.model.Usuario;
import com.artesabor.cuentas.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            UsuarioRepository usuarioRepository) {
        this.jwtService = jwtService;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String encabezado = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (encabezado == null || !encabezado.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = encabezado.substring(7);

        try {
            if (jwtService.esValido(token)) {
                String correo = jwtService.obtenerCorreo(token);

                Usuario usuario = usuarioRepository.findByCorreo(correo)
                        .filter(Usuario::isActivo)
                        .orElse(null);

                if (usuario != null
                        && SecurityContextHolder.getContext()
                                .getAuthentication() == null) {

                    var autenticacion =
                            new UsernamePasswordAuthenticationToken(
                                    usuario.getCorreo(),
                                    null,
                                    java.util.List.of(
                                            new SimpleGrantedAuthority(
                                                    "ROLE_" + usuario.getRol().name())
                                    )
                            );

                    SecurityContextHolder.getContext()
                            .setAuthentication(autenticacion);
                }
            }
        } catch (io.jsonwebtoken.JwtException
                | IllegalArgumentException error) {
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}
