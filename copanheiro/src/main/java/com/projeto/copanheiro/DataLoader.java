package com.projeto.copanheiro;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.projeto.copanheiro.repositories.UsuarioRepository;
import com.projeto.copanheiro.repositories.TipoJogoRepository;
import com.projeto.copanheiro.models.Usuario;
import com.projeto.copanheiro.models.TipoJogo;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private TipoJogoRepository tipoJogoRepository;

    @Override
    public void run(String... args) throws Exception {
        // Criar usuário padrão se não existir
        if(usuarioRepository.count() == 0) {
            Usuario u = new Usuario();
            u.setNome("João");
            u.setSaldo(1000.0);
            usuarioRepository.save(u);
            System.out.println("✅ Usuário padrão criado!");
        }
        
        // Criar tipos de jogo se não existirem
        if(tipoJogoRepository.count() == 0) {
            tipoJogoRepository.save(new TipoJogo("Avião", "Jogo do aviãozinho", 1.0, 20.0));
            tipoJogoRepository.save(new TipoJogo("Roleta", "Roleta numérica", 1.0, 5.0));
            tipoJogoRepository.save(new TipoJogo("Cassino", "Caça-níquel", 1.0, 10.0));
            System.out.println("✅ Tipos de jogo criados!");
        }
    }
}