package com.agendei.backend.service;

import com.agendei.backend.dto.CadastroClienteRequestDTO;
import com.agendei.backend.dto.CadastroClienteResponseDTO;
import com.agendei.backend.model.Cliente;
import com.agendei.backend.repository.ClienteRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ClienteService {

    private final ClienteRepository repository;
    private final PasswordEncoder passwordEncoder; // Para criptografar a senha!

    public ClienteService(ClienteRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public CadastroClienteResponseDTO cadastrar(CadastroClienteRequestDTO dto) {
        // Verifica se email já existe (Opcional, mas recomendado)
        if (repository.findByEmail(dto.getEmail()) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "O e-mail informado já está cadastrado no sistema.");
        }

        Cliente cliente = new Cliente();
        cliente.setNome(dto.getNome());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefone(dto.getTelefone());
        // Criptografa a senha antes de salvar
        cliente.setSenha(passwordEncoder.encode(dto.getSenha()));

        Cliente salvo = repository.save(cliente);

        return new CadastroClienteResponseDTO(salvo);
    }
}