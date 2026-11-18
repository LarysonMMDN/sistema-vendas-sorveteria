package com.example.AtividadeN2.service;

import com.example.AtividadeN2.entity.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    public String gerarToken(Usuario usuario) {

        SecretKey chave = Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );

        long agora = System.currentTimeMillis();

        return Jwts.builder()
                .subject(usuario.getId().toString())
                .claim("email", usuario.getEmail())
                .claim("perfil", usuario.getPerfil())
                .issuedAt(new Date(agora))
                .expiration(new Date(agora + 86400000))
                .signWith(chave)
                .compact();
    }
}
