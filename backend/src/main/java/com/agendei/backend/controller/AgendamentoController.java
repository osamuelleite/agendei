package com.agendei.backend.controller;

import com.agendei.backend.dto.AgendamentoRequestDTO;
import com.agendei.backend.dto.AgendamentoResponseDTO;
import com.agendei.backend.service.AgendamentoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    private final AgendamentoService service;

    public AgendamentoController(AgendamentoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<AgendamentoResponseDTO> agendar(@RequestBody @Valid AgendamentoRequestDTO dto) {
        AgendamentoResponseDTO novoAgendamento = service.agendar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoAgendamento);
    }
}