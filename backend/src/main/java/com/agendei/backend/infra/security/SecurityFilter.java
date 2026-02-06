package com.agendei.backend.infra.security;

import com.agendei.backend.repository.ClienteRepository; // <--- Import Novo
import com.agendei.backend.repository.ProfissionalRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails; // <--- Import Novo
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final ProfissionalRepository profissionalRepository;
    private final ClienteRepository clienteRepository; // <--- Repositório Novo

    // Construtor atualizado
    public SecurityFilter(TokenService tokenService, ProfissionalRepository profissionalRepository, ClienteRepository clienteRepository) {
        this.tokenService = tokenService;
        this.profissionalRepository = profissionalRepository;
        this.clienteRepository = clienteRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var tokenJWT = recuperarToken(request);

        if (tokenJWT != null) {
            var subject = tokenService.getSubject(tokenJWT); // Pega o e-mail de dentro do token

            // 1. Tenta achar na tabela de Profissionais
            UserDetails usuario = profissionalRepository.findByEmail(subject);

            // 2. Se não achou, tenta na tabela de Clientes (CASCATA)
            if (usuario == null) {
                usuario = clienteRepository.findByEmail(subject);
            }

            // 3. Se achou alguém (seja Profissional ou Cliente), libera a entrada
            if (usuario != null) {
                var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }
}