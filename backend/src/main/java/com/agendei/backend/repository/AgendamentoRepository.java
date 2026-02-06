package com.agendei.backend.repository;

import com.agendei.backend.model.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    // Buscar agendamentos de um profissional específico
    List<Agendamento> findByProfissionalId(Long profissionalId);

    // Busca para o Cliente (NOVO)
    // "Busque agendamentos onde o campo 'cliente' tenha o 'email' igual a X"
    List<Agendamento> findByClienteEmail(String email);

    // Verificar se já existe agendamento naquele horário para aquele profissional (Evitar conflito!)
    boolean existsByProfissionalIdAndDataHora(Long profissionalId, LocalDateTime dataHora);
}