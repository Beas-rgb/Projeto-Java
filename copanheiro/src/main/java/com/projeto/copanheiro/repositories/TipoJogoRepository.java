package com.projeto.copanheiro.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.projeto.copanheiro.models.TipoJogo;

public interface TipoJogoRepository extends JpaRepository<TipoJogo, Long> {
}