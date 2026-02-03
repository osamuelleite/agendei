package com.agendei.backend.dto;

import jakarta.validation.constraints.NotBlank; // Boa prática: validar se não está vazio
import lombok.Data;

@Data
public class LoginRequestDTO {

    @NotBlank
    private String email;

    @NotBlank
    private String senha;
}