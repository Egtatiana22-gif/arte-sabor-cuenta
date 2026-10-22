
package com.artesabor.cuentas.controller;

import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Rutas privadas para consultar el perfil autenticado. */
@RestController
@RequestMapping("/api/perfil")
public class PerfilController {

    @GetMapping
    public Map<String, String> consultarPerfil(Authentication autenticacion) {
        return Map.of(
                "mensaje", "Acceso autorizado.",
                "correo", autenticacion.getName(),
                "rol", autenticacion.getAuthorities()
                        .iterator().next().getAuthority()
        );
    }
}
