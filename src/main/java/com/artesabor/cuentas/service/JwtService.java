package com.artesabor.cuentas.service;

import com.artesabor.cuentas.model.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey claveSecreta;
    private final long duracionMs;

    public JwtService(
            @Value("${app.jwt.secret}") String secreto,
            @Value("${app.jwt.expiration-ms:3600000}") long duracionMs) {
        this.claveSecreta = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secreto));
        this.duracionMs = duracionMs;
    }

    public String generarToken(Usuario usuario) {
        Instant ahora = Instant.now();

        return Jwts.builder()
                .subject(usuario.getCorreo())
                .claim("rol", usuario.getRol().name())
                .issuedAt(Date.from(ahora))
                .expiration(Date.from(ahora.plusMillis(duracionMs)))
                .signWith(claveSecreta)
                .compact();
    }

    public String obtenerCorreo(String token) {
        return Jwts.parser()
                .verifyWith(claveSecreta)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean esValido(String token) {
        try {
            obtenerCorreo(token);
            return true;
        } catch (io.jsonwebtoken.JwtException | IllegalArgumentException error) {
            return false;
        }
    }
}
