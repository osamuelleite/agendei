package com.agendei.backend.controller;

import com.agendei.backend.dto.LoginRequestDTO;
import com.agendei.backend.dto.LoginResponseDTO;
import com.agendei.backend.infra.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails; // <--- IMPORT NOVO E OBRIGATÓRIO
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AutenticacaoController {

    private final AuthenticationManager manager;
    private final TokenService tokenService;

    public AutenticacaoController(AuthenticationManager manager, TokenService tokenService) {
        this.manager = manager;
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<LoginResponseDTO> efetuarLogin(@RequestBody @Valid LoginRequestDTO dados) {
        // 1. Cria um token simples com usuário e senha para passar pro Manager
        var tokenDeAutenticacao = new UsernamePasswordAuthenticationToken(dados.getEmail(), dados.getSenha());

        // 2. O Manager bate no banco (via AutenticacaoService), verifica a senha e devolve o objeto completo
        Authentication autenticacao = manager.authenticate(tokenDeAutenticacao);

        // 3. CORREÇÃO CRÍTICA AQUI:
        // Antes estava: (Profissional) ... -> Isso quebrava quando era Cliente.
        // Agora usamos (UserDetails) ... -> Aceita tanto Profissional quanto Cliente.
        UserDetails usuarioLogado = (UserDetails) autenticacao.getPrincipal();

        // 4. Geramos o JWT (O TokenService já foi atualizado para ler o ID de dentro do UserDetails)
        String tokenJwt = tokenService.gerarToken(usuarioLogado);

        return ResponseEntity.ok(new LoginResponseDTO(tokenJwt));
    }
}