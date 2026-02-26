package com.projeto.copanheiro.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.projeto.copanheiro.models.JogoPersonalizado;

public interface JogoPersonalizadoRepository extends JpaRepository<JogoPersonalizado, Long> {
}