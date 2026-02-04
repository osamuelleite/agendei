package com.agendei.backend.infra.security;

import com.agendei.backend.model.Cliente;       // <--- Import Novo
import com.agendei.backend.model.Profissional;  // <--- Import Novo
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails; // <--- Import Fundamental
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    // MUDANÇA: Agora aceita 'UserDetails' (Pode ser Profissional OU Cliente)
    public String gerarToken(UserDetails usuario) {
        try {
            Algorithm algoritmo = Algorithm.HMAC256(secret);

            // LÓGICA NOVA: Descobre qual o ID do usuário dependendo do tipo
            String idUsuario = null;

            if (usuario instanceof Profissional) {
                idUsuario = ((Profissional) usuario).getId().toString();
            } else if (usuario instanceof Cliente) {
                idUsuario = ((Cliente) usuario).getId().toString();
            }

            return JWT.create()
                    .withIssuer("agendei-api")
                    .withSubject(usuario.getUsername()) // getUsername() retorna o email
                    .withClaim("id", idUsuario)         // Guardamos o ID no token para usar depois!
                    .withExpiresAt(gerarDataExpiracao())
                    .sign(algoritmo);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token JWT", exception);
        }
    }

    private Instant gerarDataExpiracao() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }

    public String getSubject(String tokenJWT) {
        try {
            Algorithm algoritmo = Algorithm.HMAC256(secret);
            return JWT.require(algoritmo)
                    .withIssuer("agendei-api")
                    .build()
                    .verify(tokenJWT)
                    .getSubject();
        } catch (JWTVerificationException exception) { // Melhor usar JWTVerificationException aqui
            throw new RuntimeException("Token JWT inválido ou expirado!");
        }
    }
}