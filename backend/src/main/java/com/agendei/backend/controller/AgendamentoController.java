package com.agendei.backend.controller;

import com.agendei.backend.dto.AgendamentoRequestDTO;
import com.agendei.backend.dto.AgendamentoResponseDTO;
import com.agendei.backend.service.AgendamentoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {
    private final AgendamentoService service;

    public AgendamentoController(AgendamentoService service) {
        this.service = service;
    }

    // Rota Pública (Cliente marca horário)
    @PostMapping
    // Adicionamos 'Principal principal' para pegar o usuário logado (pode ser null se for anônimo)
    public ResponseEntity<AgendamentoResponseDTO> agendar(@RequestBody @Valid AgendamentoRequestDTO dados, Principal principal) {

        String emailClienteLogado = (principal != null) ? principal.getName() : null;

        // Passamos o email para o service
        var agendamento = service.agendar(dados, emailClienteLogado);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(agendamento.getId()).toUri();

        return ResponseEntity.created(uri).body(agendamento);
    }

    // Rota Privada (Profissional vê sua agenda)
    @GetMapping
    public ResponseEntity<List<AgendamentoResponseDTO>> listarMeusAgendamentos(Principal principal) {
        // O "Principal" vem do Token JWT. O .getName() retorna o email que estava no subject.
        String emailLogado = principal.getName();

        List<AgendamentoResponseDTO> meusAgendamentos = service.buscarMeusAgendamentos(emailLogado);

        return ResponseEntity.ok(meusAgendamentos);
    }
    // Rota: PATCH /agendamentos/{id}/confirmar
    @PatchMapping("/{id}/confirmar")
    public ResponseEntity<AgendamentoResponseDTO> confirmar(@PathVariable Long id, Principal principal) {
        // O "principal.getName()" pega o e-mail do token automaticamente
        var agendamentoAtualizado = service.confirmarAgendamento(id, principal.getName());
        return ResponseEntity.ok(agendamentoAtualizado);
    }

    // Rota: PATCH /agendamentos/{id}/cancelar
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<AgendamentoResponseDTO> cancelar(@PathVariable Long id, Principal principal) {
        var agendamentoAtualizado = service.cancelarAgendamento(id, principal.getName());
        return ResponseEntity.ok(agendamentoAtualizado);
    }

}