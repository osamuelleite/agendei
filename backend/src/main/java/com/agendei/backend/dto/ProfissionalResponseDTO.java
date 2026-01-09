package com.agendei.backend.dto;
import lombok.Data;

@Data
public class ProfissionalResponseDTO {
    private Long id;
    private String nome;
    private String email;
    private String especialidade;
}
