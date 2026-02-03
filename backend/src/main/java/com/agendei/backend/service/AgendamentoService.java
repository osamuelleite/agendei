package com.agendei.backend.service;

import com.agendei.backend.dto.AgendamentoRequestDTO;
import com.agendei.backend.dto.AgendamentoResponseDTO;
import com.agendei.backend.model.Agendamento;
import com.agendei.backend.model.Profissional;
import com.agendei.backend.repository.AgendamentoRepository;
import com.agendei.backend.repository.ProfissionalRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final ProfissionalRepository profissionalRepository;

    public AgendamentoService(AgendamentoRepository agendamentoRepository, ProfissionalRepository profissionalRepository) { // construtor de classe, realizando Injeção de dependência por construtor
        this.agendamentoRepository = agendamentoRepository; // pega o objeto que foi enviado ao construtor e o guarda no atributo interno da classe. Pertmite que usa o métodos de banco de dados (buscar e salvar) em qualquer lugar dentro da classe.
        this.profissionalRepository = profissionalRepository;
    }

    public AgendamentoResponseDTO agendar(AgendamentoRequestDTO dados) {
        // 1. Busca o profissional pelo ID (se não achar, lança erro 404)
        Profissional profissional = profissionalRepository.findById(dados.getProfissionalId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profissional não encontrado"));

        // 2. REGRA DE NEGÓCIO: Verifica se já existe agendamento nesse horário para esse profissional
        boolean horarioOcupado = agendamentoRepository.existsByProfissionalIdAndDataHora(
                profissional.getId(), dados.getDataHora());

        if (horarioOcupado) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Esse horário já está ocupado!");
        }

        // 3. Cria o objeto Agendamento e define os valores
        Agendamento agendamento = new Agendamento();
        agendamento.setProfissional(profissional);
        agendamento.setDataHora(dados.getDataHora());
        agendamento.setClienteNome(dados.getClienteNome());
        agendamento.setObservacao(dados.getObservacao());

        // 4. Salva no banco
        Agendamento agendamentoSalvo = agendamentoRepository.save(agendamento);

        // 5. Transforma em DTO para devolver pro Controller
        return new AgendamentoResponseDTO(agendamentoSalvo);
    }
    public java.util.List<AgendamentoResponseDTO> buscarMeusAgendamentos(String emailProfissional) {
        // 1. Busca o profissional pelo email (Precisamos fazer o Cast porque o repo retorna UserDetails)
        Profissional profissional = (Profissional) profissionalRepository.findByEmail(emailProfissional);

        if (profissional == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Profissional não encontrado");
        }

        // 2. Busca a lista no banco usando o ID dele
        var agendamentos = agendamentoRepository.findByProfissionalId(profissional.getId());

        // 3. Converte a lista de Entidades para lista de DTOs
        return agendamentos.stream()
                .map(AgendamentoResponseDTO::new)
                .toList();
    }
}
