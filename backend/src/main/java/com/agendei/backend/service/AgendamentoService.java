package com.agendei.backend.service;

import com.agendei.backend.dto.AgendamentoRequestDTO;
import com.agendei.backend.dto.AgendamentoResponseDTO;
import com.agendei.backend.model.Agendamento;
import com.agendei.backend.model.Profissional;
import com.agendei.backend.model.Servico;
import com.agendei.backend.repository.AgendamentoRepository;
import com.agendei.backend.repository.ClienteRepository;
import com.agendei.backend.repository.ProfissionalRepository;
import com.agendei.backend.repository.ServicoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.agendei.backend.model.Cliente;

import java.util.List;

@Service
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final ProfissionalRepository profissionalRepository;
    private final ServicoRepository servicoRepository;
    private final ClienteRepository clienteRepository; // <--- Campo Novo (Injeção)

    // Construtor atualizado com o ClienteRepository
    public AgendamentoService(AgendamentoRepository agendamentoRepository,
                              ProfissionalRepository profissionalRepository,
                              ServicoRepository servicoRepository,
                              ClienteRepository clienteRepository) {
        this.agendamentoRepository = agendamentoRepository;
        this.profissionalRepository = profissionalRepository;
        this.servicoRepository = servicoRepository;
        this.clienteRepository = clienteRepository;
    }

    public AgendamentoResponseDTO agendar(AgendamentoRequestDTO dados, String emailClienteLogado) {
        // 1. Busca Profissional
        Profissional profissional = profissionalRepository.findById(dados.getProfissionalId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profissional não encontrado"));

        // 2. Busca Serviço
        Servico servico = servicoRepository.findById(dados.getServicoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Serviço não encontrado"));

        // 3. Validação: O serviço pertence ao profissional?
        if (!servico.getProfissional().getId().equals(profissional.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Este serviço não pertence a este profissional");
        }

        // 4. Validação de Horário
        boolean horarioOcupado = agendamentoRepository.existsByProfissionalIdAndDataHora(
                profissional.getId(), dados.getDataHora());

        if (horarioOcupado) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Esse horário já está ocupado!");
        }

        // 5. Salva Agendamento
        Agendamento agendamento = new Agendamento();
        agendamento.setProfissional(profissional);
        agendamento.setServico(servico);
        agendamento.setDataHora(dados.getDataHora());
        agendamento.setClienteNome(dados.getClienteNome());
        agendamento.setObservacao(dados.getObservacao());
        agendamento.setStatus(com.agendei.backend.model.enums.StatusAgendamento.PENDENTE);

        // --- LÓGICA NOVA: VÍNCULO DO CLIENTE ---
        if (emailClienteLogado != null) {
            // Busca pelo email
            var usuario = clienteRepository.findByEmail(emailClienteLogado);

            // Verifica se quem achou é mesmo um Cliente (e não um Profissional tentando agendar pra si mesmo)
            if (usuario instanceof Cliente) {
                agendamento.setCliente((Cliente) usuario); // <--- A MÁGICA ACONTECE AQUI
            }
        }

        Agendamento agendamentoSalvo = agendamentoRepository.save(agendamento);

        return new AgendamentoResponseDTO(agendamentoSalvo);
    }

    public List<AgendamentoResponseDTO> buscarMeusAgendamentos(String emailUsuarioLogado) {

        // 1. Buscamos no repositório de profissionais
        // O retorno é UserDetails, então guardamos numa variável genérica
        org.springframework.security.core.userdetails.UserDetails usuarioEncontrado = profissionalRepository.findByEmail(emailUsuarioLogado);

        // 2. Verificamos: "Esse usuário que achei é, de fato, um Profissional?"
        if (usuarioEncontrado instanceof Profissional) {
            // Se sim, fazemos o Cast (conversão) para pegar o ID
            Profissional profissional = (Profissional) usuarioEncontrado;

            return agendamentoRepository.findByProfissionalId(profissional.getId())
                    .stream().map(AgendamentoResponseDTO::new).toList();
        }

        // 3. Se não achou como profissional, assumimos que é Cliente e buscamos pelo e-mail
        return agendamentoRepository.findByClienteEmail(emailUsuarioLogado)
                .stream().map(AgendamentoResponseDTO::new).toList();
    }

    // --- MÉTODOS DE STATUS ---

    public AgendamentoResponseDTO confirmarAgendamento(Long idAgendamento, String emailProfissional) {
        return alterarStatus(idAgendamento, emailProfissional, com.agendei.backend.model.enums.StatusAgendamento.CONFIRMADO);
    }

    public AgendamentoResponseDTO cancelarAgendamento(Long idAgendamento, String emailProfissional) {
        return alterarStatus(idAgendamento, emailProfissional, com.agendei.backend.model.enums.StatusAgendamento.CANCELADO);
    }

    private AgendamentoResponseDTO alterarStatus(Long id, String emailProfissional, com.agendei.backend.model.enums.StatusAgendamento novoStatus) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Agendamento não encontrado"));

        // SEGURANÇA: Verifica se é o dono do agendamento
        if (!agendamento.getProfissional().getEmail().equals(emailProfissional)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para alterar esse agendamento");
        }

        agendamento.setStatus(novoStatus);
        agendamentoRepository.save(agendamento);

        return new AgendamentoResponseDTO(agendamento);
    }
}