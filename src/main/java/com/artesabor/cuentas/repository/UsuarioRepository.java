
package com.artesabor.cuentas.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.artesabor.cuentas.model.Usuario;

/**
 * Acceso a los datos de los usuarios almacenados en MySQL.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Busca una cuenta por correo para validar el registro y el inicio de sesión.
    Optional<Usuario> findByCorreo(String correo);

    // Comprueba si un correo ya está registrado.
    boolean existsByCorreo(String correo);
}
