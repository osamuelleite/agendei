package com.agendei.backend.controller;

import com.agendei.backend.dto.ProfissionalRequestDTO;
import com.agendei.backend.dto.ProfissionalResponseDTO;
import com.agendei.backend.service.ProfissionalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profissionais")
public class ProfissionalController {

    private final ProfissionalService service;

    public ProfissionalController(ProfissionalService service) {
        this.service = service;
    }

    @PostMapping
    // 1. Mudamos de "Profissional" para "ProfissionalRequestDTO"
    // 2. Adicionamos @Valid para ativar as regras (tamanho, notblank, email)
    public ResponseEntity<ProfissionalResponseDTO> cadastrar(@RequestBody @Valid ProfissionalRequestDTO dto) {

        // Agora o tipo bate com o que o Service espera
        ProfissionalResponseDTO novoProfissional = service.cadastrar(dto);

        // Retorna 201 Created e o DTO de resposta (que não tem a senha)
        return ResponseEntity.status(HttpStatus.CREATED).body(novoProfissional);
    }
}