package com.agendei.backend.model;

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

    // Data e Hora do agendamento (Ex: 2025-10-20T14:30:00)
    private LocalDateTime dataHora;

    private String clienteNome; // Quem agendou? (Futuramente pode ser uma tabela User)

    private String observacao; // Ex: "Corte de cabelo e barba"

    // --- O RELACIONAMENTO MÁGICO ---
    @ManyToOne // Muitos agendamentos para Um profissional
    @JoinColumn(name = "id_profissional") // Cria a coluna 'id_profissional' no banco (Chave Estrangeira)
    private Profissional profissional;
}