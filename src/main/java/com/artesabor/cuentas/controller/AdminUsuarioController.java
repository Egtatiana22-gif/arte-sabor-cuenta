package com.artesabor.cuentas.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.artesabor.cuentas.dto.UsuarioResponse;
import com.artesabor.cuentas.model.RolUsuario;
import com.artesabor.cuentas.service.UsuarioService;

/**
 * Permite al administrador gestionar las cuentas de Arte y Sabor.
 * El acceso se restringe mediante las reglas de SecurityConfig.
 */
@RestController
@RequestMapping("/api/admin/usuarios")
public class AdminUsuarioController {

    private final UsuarioService usuarioService;

    public AdminUsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Consulta todas las cuentas registradas.
     */
    @GetMapping
    public List<UsuarioResponse> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }

    /**
     * Cambia el rol de una cuenta existente.
     * Ejemplo de cuerpo: {"rol":"EMPLEADO"}
     */
    @PatchMapping("/{id}/rol")
    public ResponseEntity<?> cambiarRol(
            @PathVariable Long id,
            @RequestBody Map<String, String> solicitud) {

        try {
            String rolTexto = solicitud.get("rol");

            if (rolTexto == null || rolTexto.isBlank()) {
                return ResponseEntity.badRequest().body(
                    Map.of("mensaje", "Debes indicar el rol.")
                );
            }

            RolUsuario nuevoRol = RolUsuario.valueOf(
                rolTexto.trim().toUpperCase()
            );

            UsuarioResponse usuario =
                usuarioService.cambiarRol(id, nuevoRol);

            return ResponseEntity.ok(usuario);

        } catch (IllegalArgumentException error) {
            return ResponseEntity.badRequest().body(
                Map.of("mensaje", error.getMessage() == null
                    ? "El rol indicado no es válido."
                    : error.getMessage())
            );
        }
    }
}
