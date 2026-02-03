package com.agendei.backend.model;

import com.agendei.backend.model.enums.StatusAgendamento;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tb_agendamento")
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataHora;

    private String clienteNome;

    private String observacao;

    // --- O CAMPO NOVO QUE TINHA SUMIDO ---
    @Enumerated(EnumType.STRING)
    private StatusAgendamento status = StatusAgendamento.PENDENTE; // Nasce como PENDENTE

    @ManyToOne
    @JoinColumn(name = "id_profissional")
    private Profissional profissional;

    @ManyToOne
    @JoinColumn(name = "id_servico")
    private Servico servico;
}