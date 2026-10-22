
package com.artesabor.cuentas.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.artesabor.cuentas.dto.LoginRequest;
import com.artesabor.cuentas.dto.RegistroRequest;
import com.artesabor.cuentas.dto.UsuarioResponse;
import com.artesabor.cuentas.service.AuthService;
import com.artesabor.cuentas.service.UsuarioService;

/**
 * Controlador para el registro y el inicio de sesión.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioService usuarioService;
    private final AuthService authService;

    public AuthController(
            UsuarioService usuarioService,
            AuthService authService) {
        this.usuarioService = usuarioService;
        this.authService = authService;
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registrar(
            @RequestBody RegistroRequest solicitud) {
        try {
            UsuarioResponse usuario = usuarioService.registrar(solicitud);

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "mensaje", "Cuenta creada correctamente.",
                "usuario", usuario
            ));
        } catch (IllegalArgumentException error) {
            return ResponseEntity.badRequest().body(Map.of(
                "mensaje", error.getMessage()
            ));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> iniciarSesion(
            @RequestBody LoginRequest solicitud) {
        try {
            return ResponseEntity.ok(authService.iniciarSesion(solicitud));
        } catch (IllegalArgumentException error) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                "mensaje", error.getMessage()
            ));
        }
    }
}
