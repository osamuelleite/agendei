package com.agendei.backend.dto;

import com.agendei.backend.model.Cliente;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CadastroClienteRequestDTO {

    @NotBlank(message = "O nome é obrigatório") // Não aceita null nem "" nem" "
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
    private String nome;

    private String nomeServico;
    @NotBlank(message = "O telefone é obrigatório")
    private String telefone;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "O e-mail deve ser válido") // Verifica se tem @ e ponto
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    private String senha;
}
