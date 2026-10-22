
package com.artesabor.cuentas.dto;

/**
 * Datos requeridos para iniciar sesión en Arte y Sabor.
 */
public record LoginRequest(String correo, String contrasena) {
}
