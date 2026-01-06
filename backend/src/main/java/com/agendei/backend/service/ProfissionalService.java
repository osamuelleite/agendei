package com.agendei.backend.service;

import com.agendei.backend.model.Profissional;
import com.agendei.backend.repository.ProfissionalRepository;
import org.springframework.stereotype.Service;

@Service

public class ProfissionalService {
    private final ProfissionalRepository repository;
    public ProfissionalService (ProfissionalRepository repository){
        this.repository = repository;
    }
    public Profissional cadastrar(Profissional profissional) {
        // regra de negócio: verificar se o email já existe antes de salvar
        return repository.save(profissional);
    }
}
