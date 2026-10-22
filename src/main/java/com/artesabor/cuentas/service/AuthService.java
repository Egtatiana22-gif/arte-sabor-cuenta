
package com.artesabor.cuentas.service;

import com.artesabor.cuentas.dto.LoginRequest;
import com.artesabor.cuentas.dto.UsuarioResponse;
import com.artesabor.cuentas.model.Usuario;
import com.artesabor.cuentas.repository.UsuarioRepository;
import java.util.Map;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public Map<String, Object> iniciarSesion(LoginRequest solicitud) {
        if (solicitud == null
                || solicitud.correo() == null
                || solicitud.correo().isBlank()
                || solicitud.contrasena() == null
                || solicitud.contrasena().isBlank()) {
            throw new IllegalArgumentException(
                    "Debes ingresar el correo y la contraseña.");
        }

        String correo = solicitud.correo().trim().toLowerCase();

        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Correo o contraseña incorrectos."));

        if (!usuario.isActivo()
                || !passwordEncoder.matches(
                        solicitud.contrasena(), usuario.getContrasena())) {
            throw new IllegalArgumentException(
                    "Correo o contraseña incorrectos.");
        }

        String token = jwtService.generarToken(usuario);

        return Map.of(
                "mensaje", "Inicio de sesión correcto.",
                "token", token,
                "tipo", "Bearer",
                "usuario", UsuarioResponse.desde(usuario)
        );
    }
}
