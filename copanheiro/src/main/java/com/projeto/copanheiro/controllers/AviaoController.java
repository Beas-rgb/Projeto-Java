package com.projeto.copanheiro.controllers;

import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.projeto.copanheiro.models.Usuario;
import com.projeto.copanheiro.repositories.UsuarioRepository;
import com.projeto.copanheiro.services.UsuarioService;

@Controller
@RequestMapping("/aviao")
public class AviaoController {

    @Autowired
    private UsuarioRepository repository;
    
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String aviao(Model model) {
        model.addAttribute("usuario", usuarioService.getUsuario());
        return "aviao";
    }

    @PostMapping("/jogar")
    public String jogar(@RequestParam double retirada, @RequestParam double valor, Model model) {

        Usuario usuario = usuarioService.getUsuario();

        if (valor <= 0) {
            model.addAttribute("erro", "Valor inválido! Digite um valor positivo.");
        } else if (valor > usuario.getSaldo()) {
            model.addAttribute("erro", "Saldo insuficiente! Seu saldo é R$ " + String.format("%.2f", usuario.getSaldo()));
        } else {
            Random random = new Random();
            double multiplicador = 1.0 + (random.nextDouble() * 19.0);
            multiplicador = Math.round(multiplicador * 10) / 10.0;

            // Se retirada for 0, significa que o avião caiu
            if (retirada <= 0) {
                usuario.setSaldo(usuario.getSaldo() - valor);
                model.addAttribute("resultado", "💥 O avião caiu! Você perdeu R$" + String.format("%.2f", valor) + 
                                               "! Multiplicador: " + multiplicador + "x");
            } else if (retirada <= multiplicador) {
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