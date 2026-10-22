package com.artesabor.cuentas.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador para comprobar el estado del servicio web.
 */
@RestController
@RequestMapping("/api")
public class EstadoController {

    /**
     * Verifica que la API esté funcionando.
     * Endpoint: GET /api/estado
     */
    @GetMapping("/estado")
    public Map<String, String> consultarEstado() {
        return Map.of(
            "servicio", "Arte y Sabor",
            "estado", "activo",
            "mensaje", "El servicio web funciona correctamente"
        );
    }
}