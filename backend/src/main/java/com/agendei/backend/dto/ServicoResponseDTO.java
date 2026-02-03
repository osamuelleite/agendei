package com.agendei.backend.dto;

import com.agendei.backend.model.Servico;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ServicoResponseDTO {
    private Long id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private Integer duracaoMinutos;

    public ServicoResponseDTO(Servico servico) {
        this.id = servico.getId();
        this.nome = servico.getNome();
        this.descricao = servico.getDescricao();
        this.preco = servico.getPreco();
        this.duracaoMinutos = servico.getDuracaoMinutos();
    }
}