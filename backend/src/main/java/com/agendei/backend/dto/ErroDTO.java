package com.agendei.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ErroDTO {
    private int status; //EX: 400, 404, 409
    private String mensagem; //Ex: "Erro na validação dos cmapos"
    private LocalDateTime dataHora; //Quando aconteceu
    private List <String> erros; //Lista detalhada (Ex: "A senha deve ter 6 caracteres")
}
