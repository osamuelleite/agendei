package com.agendei.backend.controller;

import com.agendei.backend.dto.ServicoRequestDTO;
import com.agendei.backend.dto.ServicoResponseDTO;
import com.agendei.backend.service.ServicoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/servicos")
public class ServicoController {

    private final ServicoService service;

    public ServicoController(ServicoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ServicoResponseDTO> cadastrar(@RequestBody @Valid ServicoRequestDTO dto, Principal principal) {
        ServicoResponseDTO novoServico = service.cadastrar(dto, principal.getName());

        // Cria a URL do novo recurso (boa prática REST)
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(novoServico.getId()).toUri();

        return ResponseEntity.created(uri).body(novoServico);
    }

    @GetMapping("/meus")
    public ResponseEntity<List<ServicoResponseDTO>> listarMeusServicos(Principal principal) {
        List<ServicoResponseDTO> lista = service.listarMeusServicos(principal.getName());
        return ResponseEntity.ok(lista);
    }
}