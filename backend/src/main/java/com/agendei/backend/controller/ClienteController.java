package com.agendei.backend.controller;

import com.agendei.backend.dto.CadastroClienteRequestDTO;
import com.agendei.backend.dto.CadastroClienteResponseDTO;
import com.agendei.backend.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CadastroClienteResponseDTO> cadastrar(@RequestBody @Valid CadastroClienteRequestDTO dto) {
        CadastroClienteResponseDTO novoCliente = service.cadastrar(dto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(novoCliente.getId()).toUri();

        return ResponseEntity.created(uri).body(novoCliente);
    }
}