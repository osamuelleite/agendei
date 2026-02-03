package com.agendei.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ServicoRequestDTO {

    @NotBlank(message = "O nome do serviço é obrigatório")
    private String nome;

    @NotBlank(message = "A descrição é obrigatória")
    private String descricao;

    @NotNull(message = "O preço é obrigatório")
    @Min(value = 1, message = "O preço deve ser maior que zero")
    private BigDecimal preco;

    @NotNull(message = "A duração é obrigatória")
    @Min(value = 5, message = "A duração mínima é de 5 minutos")
    private Integer duracaoMinutos;
}