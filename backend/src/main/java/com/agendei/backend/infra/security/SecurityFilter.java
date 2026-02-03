package com.agendei.backend.infra.security;

import com.agendei.backend.repository.ProfissionalRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final ProfissionalRepository repository;

    public SecurityFilter(TokenService tokenService, ProfissionalRepository repository) {
        this.tokenService = tokenService;
        this.repository = repository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1. Recupera o token do cabeçalho (se houver)
        var tokenJWT = recuperarToken(request);

        if (tokenJWT != null) {
            // 2. Valida o token e pega o e-mail
            var subject = tokenService.getSubject(tokenJWT);

            // 3. Busca o usuário no banco
            UserDetails usuario = repository.findByEmail(subject);

            // 4. Diz para o Spring Security: "Esse usuário está logado!"
            var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 5. Segue o baile (passa para o próximo filtro ou controller)
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