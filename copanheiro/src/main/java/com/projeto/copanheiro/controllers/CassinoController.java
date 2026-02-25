package com.projeto.copanheiro.controllers;

import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.projeto.copanheiro.models.Usuario;
import com.projeto.copanheiro.repositories.UsuarioRepository;

@Controller
@RequestMapping("/cassino")
public class CassinoController {

    @Autowired
    private UsuarioRepository repository;

    @GetMapping
    public String cassino(Model model) {
        model.addAttribute("usuario", repository.findAll().get(0));
        return "cassino";
    }

    @PostMapping("/jogar")
    public String jogar(@RequestParam double valor, Model model) {

        Usuario usuario = repository.findAll().get(0);
        Random random = new Random();

        if (valor <= 0) {
            model.addAttribute("erro", "Valor inválido!");
        } else if (valor > usuario.getSaldo()) {
            model.addAttribute("erro", "Saldo insuficiente!");
        } else {
            int n1 = random.nextInt(5) + 1;
            int n2 = random.nextInt(5) + 1;
            int n3 = random.nextInt(5) + 1;
            
            double premio = 0;
            String mensagem = "";

            if (n1 == n2 && n2 == n3) {
                premio = valor * 10;
                usuario.setSaldo(usuario.getSaldo() + premio);
                mensagem = "JACKPOT! Três iguais! Ganhou R$" + String.format("%.2f", premio);
            } else if (n1 == n2 || n1 == n3 || n2 == n3) {
                premio = valor * 3;
                usuario.setSaldo(usuario.getSaldo() + premio);
                mensagem = "Parabéns! Dois iguais! Ganhou R$" + String.format("%.2f", premio);
            } else {
                usuario.setSaldo(usuario.getSaldo() - valor);
                mensagem = "Que pena! Perdeu R$" + String.format("%.2f", valor);
            }

            repository.save(usuario);
            
            model.addAttribute("resultado", n1 + " | " + n2 + " | " + n3);
            model.addAttribute("mensagem", mensagem);
        }

        model.addAttribute("usuario", usuario);
        return "cassino";
    }
}