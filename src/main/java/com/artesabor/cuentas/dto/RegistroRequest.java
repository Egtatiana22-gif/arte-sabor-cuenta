
package com.artesabor.cuentas.dto;

/**
 * Datos que recibe la API al registrar una cuenta nueva.
 */
public record RegistroRequest(
    String nombre,
    String correo,
    String contrasena
) {
}

