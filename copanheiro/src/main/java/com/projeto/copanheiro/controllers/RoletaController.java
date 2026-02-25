package com.projeto.copanheiro.controllers;

import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.projeto.copanheiro.models.Usuario;
import com.projeto.copanheiro.repositories.UsuarioRepository;

@Controller
@RequestMapping("/roleta")
public class RoletaController {

    @Autowired
    private UsuarioRepository repository;

    @GetMapping
    public String roleta(Model model) {
        model.addAttribute("usuario", repository.findAll().get(0));
        return "roleta";
    }

    @PostMapping("/jogar")
    public String jogar(@RequestParam int numero, @RequestParam double valor, Model model) {

        Usuario usuario = repository.findAll().get(0);

        if (valor <= 0) {
            model.addAttribute("erro", "Valor inválido!");
        } else if (numero < 0 || numero > 9) {
            model.addAttribute("erro", "Número deve estar entre 0 e 9!");
        } else if (valor > usuario.getSaldo()) {
            model.addAttribute("erro", "Saldo insuficiente!");
        } else {
            int sorteado = new Random().nextInt(10);

            if (numero == sorteado) {
                double ganho = valor * 5;
                usuario.setSaldo(usuario.getSaldo() + ganho);
                model.addAttribute("resultado", "Ganhou R$" + String.format("%.2f", ganho) + "! Número sorteado: " + sorteado);
            } else {
                usuario.setSaldo(usuario.getSaldo() - valor);
                model.addAttribute("resultado", "Perdeu R$" + String.format("%.2f", valor) + "! Número sorteado: " + sorteado);
            }

            repository.save(usuario);
        }

        model.addAttribute("usuario", usuario);
        return "roleta";
    }
}