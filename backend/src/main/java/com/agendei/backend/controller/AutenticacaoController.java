package com.agendei.backend.controller;

import com.agendei.backend.dto.LoginRequestDTO;
import com.agendei.backend.dto.LoginResponseDTO;
import com.agendei.backend.model.Profissional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.agendei.backend.infra.security.TokenService;

@RestController
@RequestMapping("/login")
public class AutenticacaoController {

    private final AuthenticationManager manager; // O Spring Security que gerencia o login
    private final TokenService tokenService; // Nossa fábrica de tokens

    public AutenticacaoController(AuthenticationManager manager, TokenService tokenService) {
        this.manager = manager;
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<LoginResponseDTO> efetuarLogin(@RequestBody @Valid LoginRequestDTO dados) {
        // 1. Cria um token simples com usuário e senha (ainda não é o JWT)
        var tokenDeAutenticacao = new UsernamePasswordAuthenticationToken(dados.getEmail(), dados.getSenha());

        // 2. O Manager bate no banco, verifica o hash da senha e devolve o usuário logado
        Authentication autenticacao = manager.authenticate(tokenDeAutenticacao);

        // 3. Se chegou aqui, a senha está certa. Pegamos o usuário.
        Profissional profissionalLogado = (Profissional) autenticacao.getPrincipal();

        // 4. Geramos o JWT (A Pulseira da Balada)
        String tokenJwt = tokenService.gerarToken(profissionalLogado);

        return ResponseEntity.ok(new LoginResponseDTO(tokenJwt));
    }
}