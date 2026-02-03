package com.agendei.backend.service;

import com.agendei.backend.repository.ProfissionalRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AutenticacaoService implements UserDetailsService {

    private final ProfissionalRepository repository;

    public AutenticacaoService(ProfissionalRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // O "username" no nosso caso é o email
        return repository.findByEmail(username);
    }
}