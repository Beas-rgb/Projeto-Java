package com.projeto.copanheiro.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.projeto.copanheiro.models.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}