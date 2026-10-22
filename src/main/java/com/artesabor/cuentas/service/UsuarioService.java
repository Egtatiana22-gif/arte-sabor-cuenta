
package com.artesabor.cuentas.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.artesabor.cuentas.dto.RegistroRequest;
import com.artesabor.cuentas.dto.UsuarioResponse;
import com.artesabor.cuentas.model.RolUsuario;
import com.artesabor.cuentas.model.Usuario;
import com.artesabor.cuentas.repository.UsuarioRepository;

/**
 * Gestiona el registro de las cuentas de Arte y Sabor.
 */
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registra una cuenta nueva y asigna siempre el rol CLIENTE.
     */
    @Transactional
    public UsuarioResponse registrar(RegistroRequest solicitud) {
        if (solicitud.nombre() == null || solicitud.nombre().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }

        if (solicitud.correo() == null || solicitud.correo().isBlank()) {
            throw new IllegalArgumentException("El correo es obligatorio.");
        }

        if (solicitud.contrasena() == null
                || solicitud.contrasena().length() < 8
                || solicitud.contrasena().length() > 72) {
            throw new IllegalArgumentException(
                "La contraseña debe tener entre 8 y 72 caracteres."
            );
        }

        String nombre = solicitud.nombre().trim();
        String correo = solicitud.correo().trim().toLowerCase();

        if (nombre.length() > 100) {
            throw new IllegalArgumentException(
                "El nombre no puede superar los 100 caracteres."
            );
        }

        if (correo.length() > 150
                || !correo.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new IllegalArgumentException(
                "Debes ingresar un correo electrónico válido."
            );
        }

        if (usuarioRepository.existsByCorreo(correo)) {
            throw new IllegalArgumentException(
                "Este correo ya está registrado."
            );
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setCorreo(correo);
        usuario.setContrasena(
            passwordEncoder.encode(solicitud.contrasena())
        );

        // El rol se determina en el servidor, nunca desde la solicitud pública.
        usuario.setRol(RolUsuario.CLIENTE);
        usuario.setActivo(true);

        Usuario guardado = usuarioRepository.save(usuario);

        return UsuarioResponse.desde(guardado);
    }
}
