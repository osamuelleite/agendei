package com.agendei.backend.service;

import com.agendei.backend.dto.ServicoRequestDTO;
import com.agendei.backend.dto.ServicoResponseDTO;
import com.agendei.backend.model.Profissional;
import com.agendei.backend.model.Servico;
import com.agendei.backend.repository.ProfissionalRepository;
import com.agendei.backend.repository.ServicoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ServicoService {

    private final ServicoRepository servicoRepository;
    private final ProfissionalRepository profissionalRepository;

    public ServicoService(ServicoRepository servicoRepository, ProfissionalRepository profissionalRepository) {
        this.servicoRepository = servicoRepository;
        this.profissionalRepository = profissionalRepository;
    }

    public ServicoResponseDTO cadastrar(ServicoRequestDTO dto, String emailProfissional) {
        // 1. Busca quem está logado
        Profissional profissional = (Profissional) profissionalRepository.findByEmail(emailProfissional);

        if (profissional == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Profissional não encontrado");
        }

        // 2. Cria o serviço
        Servico servico = new Servico();
        servico.setNome(dto.getNome());
        servico.setDescricao(dto.getDescricao());
        servico.setPreco(dto.getPreco());
        servico.setDuracaoMinutos(dto.getDuracaoMinutos());
        servico.setProfissional(profissional); // Vínculo importante!
        servico.setAtivo(true);

        // 3. Salva
        servicoRepository.save(servico);

        return new ServicoResponseDTO(servico);
    }

    public List<ServicoResponseDTO> listarMeusServicos(String emailProfissional) {
        Profissional profissional = (Profissional) profissionalRepository.findByEmail(emailProfissional);

        return servicoRepository.findByProfissionalIdAndAtivoTrue(profissional.getId())
                .stream()
                .map(ServicoResponseDTO::new)
                .toList();
    }
}