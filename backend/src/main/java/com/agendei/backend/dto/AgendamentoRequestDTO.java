package com.agendei.backend.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AgendamentoRequestDTO {

    @NotNull(message = "O horário é obrigatório")
    @Future(message = "O agendamento deve ser para uma data futura") // O Java valida se não é no passado!
    private LocalDateTime dataHora;

    @NotBlank(message = "O nome do cliente é obrigatório")
    private String clienteNome;

    private String observacao; // não obrigatório

    @NotNull(message = "É necessário informar o profissional")
    private Long profissionalId; // Aqui recebemos só o ID, o Service vai buscar o objeto completo
    // Agora o cliente manda o ID do serviço
    @NotNull(message = "O serviço é obrigatório")
    private Long servicoId;
}