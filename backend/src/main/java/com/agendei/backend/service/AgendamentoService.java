package com.agendei.backend.service;

import com.agendei.backend.dto.AgendamentoRequestDTO;
import com.agendei.backend.dto.AgendamentoResponseDTO;
import com.agendei.backend.model.Agendamento;
import com.agendei.backend.model.Profissional;
import com.agendei.backend.model.Servico;
import com.agendei.backend.repository.AgendamentoRepository;
import com.agendei.backend.repository.ProfissionalRepository;
import com.agendei.backend.repository.ServicoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final ProfissionalRepository profissionalRepository;
    private final ServicoRepository servicoRepository;


    public AgendamentoService(AgendamentoRepository agendamentoRepository, ProfissionalRepository profissionalRepository, ServicoRepository servicoRepository) { // construtor de classe, realizando Injeção de dependência por construtor
        this.agendamentoRepository = agendamentoRepository; // pega o objeto que foi enviado ao construtor e o guarda no atributo interno da classe. Pertmite que usa o métodos de banco de dados (buscar e salvar) em qualquer lugar dentro da classe.
        this.profissionalRepository = profissionalRepository;
        this.servicoRepository = servicoRepository;
    }

    public AgendamentoResponseDTO agendar(AgendamentoRequestDTO dados) {
        // 1. Busca o profissional pelo ID (se não achar, lança erro 404)
        Profissional profissional = profissionalRepository.findById(dados.getProfissionalId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profissional não encontrado"));
        // 2. Busca Serviço (NOVO)
        Servico servico = servicoRepository.findById(dados.getServicoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Serviço não encontrado"));

        // 3. Validação: O serviço pertence ao profissional escolhido?
        if (!servico.getProfissional().getId().equals(profissional.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Este serviço não pertence a este profissional");
        }

        // 4. REGRA DE NEGÓCIO: Verifica se já existe agendamento nesse horário para esse profissional
        boolean horarioOcupado = agendamentoRepository.existsByProfissionalIdAndDataHora(
                profissional.getId(), dados.getDataHora());

        if (horarioOcupado) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Esse horário já está ocupado!");
        }

        // 3. Cria o objeto Agendamento e define os valores
        Agendamento agendamento = new Agendamento();
        agendamento.setProfissional(profissional);
        agendamento.setServico(servico);
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
    // Metodo para confirmar
    public AgendamentoResponseDTO confirmarAgendamento(Long idAgendamento, String emailProfissional) {
        return alterarStatus(idAgendamento, emailProfissional, com.agendei.backend.model.enums.StatusAgendamento.CONFIRMADO);
    }

    // Metodo para cancelar
    public AgendamentoResponseDTO cancelarAgendamento(Long idAgendamento, String emailProfissional) {
        return alterarStatus(idAgendamento, emailProfissional, com.agendei.backend.model.enums.StatusAgendamento.CANCELADO);
    }

    // Metodo auxiliar (privado) para evitar repetir código
    private AgendamentoResponseDTO alterarStatus(Long id, String emailProfissional, com.agendei.backend.model.enums.StatusAgendamento novoStatus) {
        // 1. Busca o agendamento
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Agendamento não encontrado"));

        // 2. SEGURANÇA: Verifica se o agendamento pertence ao profissional logado
        if (!agendamento.getProfissional().getEmail().equals(emailProfissional)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para alterar esse agendamento");
        }

        // 3. Atualiza e salva
        agendamento.setStatus(novoStatus);
        agendamentoRepository.save(agendamento);

        return new AgendamentoResponseDTO(agendamento);
    }
}
