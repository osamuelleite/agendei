package com.agendei.backend.controller;
//serve para criar o endereço "/profissionais" no servidor

import com.agendei.backend.model.Profissional;
import com.agendei.backend.service.ProfissionalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController //inicia a API REST com respostas em JSON
@RequestMapping("/profissionais") //Endereço base http://localhost:8080/profissionais

public class ProfissionalController {
    private final ProfissionalService service;

    public ProfissionalController(ProfissionalService service){
        this.service = service;
    }
    @PostMapping
    public ResponseEntity<Profissional> cadastrar(@RequestBody Profissional profissional){
        //recebe o JSON e transforma em um objeto Java (@requestBody)
        Profissional novoProfissional = service.cadastrar(profissional);
        //Responde com status 201 Created e o objeto é criado
        //comentario
        return ResponseEntity.status(HttpStatus.CREATED).body(novoProfissional);
    }
}
