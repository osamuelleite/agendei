package com.agendei.backend.dto;

import com.agendei.backend.model.Agendamento;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AgendamentoResponseDTO {
    private Long id;
    private LocalDateTime dataHora;
    private String clienteNome;
    private String observacao;
    private String nomeProfissional; // Vamos devolver só o nome, fica mais limpo

    // Construtor que converte a Entidade em DTO
    public AgendamentoResponseDTO(Agendamento agendamento) {
        this.id = agendamento.getId();
        this.dataHora = agendamento.getDataHora();
        this.clienteNome = agendamento.getClienteNome();
        this.observacao = agendamento.getObservacao();
        this.nomeProfissional = agendamento.getProfissional().getNome();
    }
}