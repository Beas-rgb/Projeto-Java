package com.projeto.copanheiro.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.projeto.copanheiro.models.Usuario;
import com.projeto.copanheiro.repositories.UsuarioRepository;

@Component
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    public Usuario getUsuario() {
        if (repository.count() == 0) {
            Usuario u = new Usuario();
            u.setNome("Jogador");
            u.setSaldo(1000.0);
            System.out.println("Criando novo usuário com saldo: " + u.getSaldo());
            return repository.save(u);
        }
        
        Usuario u = repository.findAll().get(0);
        System.out.println("UsuarioService - Usuário encontrado: " + u.getNome() + ", Saldo: " + u.getSaldo());
        return u;
    }
}