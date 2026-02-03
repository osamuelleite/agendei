package com.agendei.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal; // Importante para dinheiro!

@Data
@Entity
@Table(name = "tb_servico")
public class Servico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome; // Ex: "Corte de Cabelo"

    @Column(nullable = false)
    private String descricao; // Ex: "Corte na tesoura e máquina com acabamento"

    @Column(nullable = false)
    private BigDecimal preco; // Ex: 50.00 (Nunca use Double para dinheiro!)

    @Column(nullable = false)
    private Integer duracaoMinutos; // Ex: 30, 60

    // Vínculo: Todo serviço pertence a um profissional
    @ManyToOne
    @JoinColumn(name = "id_profissional")
    private Profissional profissional;

    // Campo para "Apagar" logicamente (Soft Delete) - Opcional, mas boa prática
    private boolean ativo = true;
}