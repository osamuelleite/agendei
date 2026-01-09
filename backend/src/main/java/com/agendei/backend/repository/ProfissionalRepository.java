package com.agendei.backend.repository;

import com.agendei.backend.model.Profissional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {
    // Só de estar vazia assim, ela já tem o poder de fazer CRUD:
    // save(), findAll(), findById(), delete()...

    // Se precisarmos buscar por email no futuro, bastaria adicionar:
    // boolean existsByEmail(String email);
}