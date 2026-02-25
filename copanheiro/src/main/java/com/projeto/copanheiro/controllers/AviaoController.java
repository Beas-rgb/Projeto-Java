package com.projeto.copanheiro.controllers;

import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.projeto.copanheiro.models.Usuario;
import com.projeto.copanheiro.repositories.UsuarioRepository;

@Controller
@RequestMapping("/aviao")
public class AviaoController {

    @Autowired
    private UsuarioRepository repository;

    private Usuario getUsuario() {
        if (repository.count() == 0) {
            Usuario u = new Usuario();
            u.setNome("João");
            u.setSaldo(1000.0);
            return repository.save(u);
        }
        return repository.findAll().get(0);
    }

    @GetMapping
    public String aviao(Model model) {
        model.addAttribute("usuario", getUsuario());
        return "aviao";
    }

    @PostMapping("/jogar")
    public String jogar(@RequestParam double retirada, @RequestParam double valor, Model model) {

        Usuario usuario = getUsuario();

        if (valor <= 0) {
            model.addAttribute("erro", "Valor inválido! Digite um valor positivo.");
        } else if (valor > usuario.getSaldo()) {
            model.addAttribute("erro", "Saldo insuficiente! Seu saldo é R$ " + String.format("%.2f", usuario.getSaldo()));
        } else {
            Random random = new Random();
            // Gera multiplicador decimal entre 1.0 e 20.0
            double multiplicador = 1.0 + (random.nextDouble() * 19.0);
            // Arredonda para 1 casa decimal
            multiplicador = Math.round(multiplicador * 10) / 10.0;

            if (retirada <= multiplicador) {
                double ganho = valor * retirada;
                usuario.setSaldo(usuario.getSaldo() + ganho);
                model.addAttribute("resultado", "🎉 Você ganhou R$" + String.format("%.2f", ganho) + 
                                               "! Multiplicador: " + multiplicador + "x (Retirou em " + retirada + "x)");
            } else {
                usuario.setSaldo(usuario.getSaldo() - valor);
                model.addAttribute("resultado", "💥 O avião caiu! Você perdeu R$" + String.format("%.2f", valor) + 
                                               "! Multiplicador: " + multiplicador + "x");
            }

            repository.save(usuario);
        }

        model.addAttribute("usuario", usuario);
        return "aviao";
    }
}