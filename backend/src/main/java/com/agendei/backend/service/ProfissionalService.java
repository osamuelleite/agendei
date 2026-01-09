package com.agendei.backend.service;


import com.agendei.backend.dto.ProfissionalRequestDTO;
import com.agendei.backend.dto.ProfissionalResponseDTO;
import com.agendei.backend.model.Profissional;
import com.agendei.backend.repository.ProfissionalRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProfissionalService {

    private final ProfissionalRepository repository;

    public ProfissionalService(ProfissionalRepository repository) {
        this.repository = repository;
    }

    public ProfissionalResponseDTO cadastrar(ProfissionalRequestDTO dto) {
        // 1. REGRA DE NEGÓCIO: Validação de E-mail Único
        if (repository.existsByEmail(dto.getEmail())) {
            // Lança um erro 409 (Conflict) se já existir
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Este e-mail já está cadastrado.");
        }

        // 2. Converter DTO (entrada) para Entidade (banco)
        Profissional entidade = new Profissional();
        entidade.setNome(dto.getNome());
        entidade.setEmail(dto.getEmail());
        entidade.setSenha(dto.getSenha());
        entidade.setEspecialidade(dto.getEspecialidade());

        // 3. Salvar no Banco
        Profissional profissionalSalvo = repository.save(entidade);

        // 4. Converter Entidade (banco) para DTO (saída)
        ProfissionalResponseDTO resposta = new ProfissionalResponseDTO();
        resposta.setId(profissionalSalvo.getId());
        resposta.setNome(profissionalSalvo.getNome());
        resposta.setEmail(profissionalSalvo.getEmail());
        resposta.setEspecialidade(profissionalSalvo.getEspecialidade());

        return resposta;
    }
}