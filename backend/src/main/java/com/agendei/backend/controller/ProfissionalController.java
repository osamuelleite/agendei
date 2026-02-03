package com.agendei.backend.controller;

import com.agendei.backend.dto.ProfissionalRequestDTO;
import com.agendei.backend.dto.ProfissionalResponseDTO;
import com.agendei.backend.service.ProfissionalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profissionais")
public class ProfissionalController {

    private final ProfissionalService service;

    public ProfissionalController(ProfissionalService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ProfissionalResponseDTO> cadastrar(@RequestBody @Valid ProfissionalRequestDTO dto) {
        ProfissionalResponseDTO novoProfissional = service.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoProfissional);
    }

    // --- NOVO METODO ---
    @GetMapping
    public ResponseEntity<String> testeDeSeguranca() {
        return ResponseEntity.ok("Parabéns! Se você está lendo isso, seu Token JWT foi aceito e você está autenticado.");
    }
}