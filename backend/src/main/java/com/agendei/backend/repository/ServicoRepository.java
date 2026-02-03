package com.agendei.backend.repository;

import com.agendei.backend.model.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> {

    // Buscar todos os serviços ativos de um profissional
    List<Servico> findByProfissionalIdAndAtivoTrue(Long idProfissional);
}