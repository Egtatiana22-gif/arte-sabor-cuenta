
package com.artesabor.cuentas.dto;

import java.time.LocalDateTime;

import com.artesabor.cuentas.model.RolUsuario;
import com.artesabor.cuentas.model.Usuario;

/**
 * Respuesta pública de una cuenta, sin incluir la contraseña.
 */
public record UsuarioResponse(
    Long id,
    String nombre,
    String correo,
    RolUsuario rol,
    boolean activo,
    LocalDateTime fechaCreacion
) {

    /**
     * Convierte una entidad Usuario en una respuesta segura para la API.
     */
    public static UsuarioResponse desde(Usuario usuario) {
        return new UsuarioResponse(
            usuario.getId(),
            usuario.getNombre(),
            usuario.getCorreo(),
            usuario.getRol(),
            usuario.isActivo(),
            usuario.getFechaCreacion()
        );
    }
}
