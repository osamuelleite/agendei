package com.agendei.backend.dto;

import com.agendei.backend.model.Cliente;
import lombok.Data;

@Data
public class CadastroClienteResponseDTO {
    private Long id;
    private String nome;
    private String email;
    private String telefone;

    public CadastroClienteResponseDTO(Cliente cliente) {
        this.id = cliente.getId();
        this.nome = cliente.getNome();
        this.email = cliente.getEmail();
        this.telefone = cliente.getTelefone();
    }
}
