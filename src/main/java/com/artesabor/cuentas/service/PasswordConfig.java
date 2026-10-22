
package com.artesabor.cuentas.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configura el mecanismo de protección de contraseñas.
 */
@Configuration
public class PasswordConfig {

    /**
     * BCrypt genera un hash seguro para almacenar contraseñas.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
