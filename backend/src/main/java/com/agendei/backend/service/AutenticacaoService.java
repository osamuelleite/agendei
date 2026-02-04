package com.agendei.backend.service;

import com.agendei.backend.repository.ClienteRepository;
import com.agendei.backend.repository.ProfissionalRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AutenticacaoService implements UserDetailsService {

    private final ProfissionalRepository profissionalRepository;
    private final ClienteRepository clienteRepository;

    public AutenticacaoService(ProfissionalRepository profissionalRepository, ClienteRepository clienteRepository) {
        this.profissionalRepository = profissionalRepository;
        this.clienteRepository = clienteRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Primeiro tenta achar Profissional
        UserDetails profissional = profissionalRepository.findByEmail(username);
        if (profissional != null) {
            return profissional;
        }

        // 2. Se não achou, tenta achar Cliente (CASCATA)
        UserDetails cliente = clienteRepository.findByEmail(username);
        if (cliente != null) {
            return cliente;
        }

        // 3. Se não achou em lugar nenhum, erro
        throw new UsernameNotFoundException("Usuário não encontrado");
    }
}