package com.agendei.backend.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProfissionalRequestDTO {
    @NotBlank(menssage = "O Nome é obrigatório")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
    private String nome;

    @NotBlank (message = "O email é obrigatório")
    @Email(message = "insira um email válido")
    private String email;

    @NotBlank(message = "a senha é obrigatória")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    private String senha;

    @NotBlank(message = "A especialidade é obrigatória")
    private String especialidade;
}
