package com.projeto.copanheiro.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.projeto.copanheiro.repositories.UsuarioRepository;
import com.projeto.copanheiro.models.Usuario;

@Controller
public class HomeController {

    @Autowired
    private UsuarioRepository repository;

    private Usuario getUsuario() {
        return repository.findAll().get(0);
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("usuario", getUsuario());
        return "home";
    }

    @GetMapping("/saldo")
    @ResponseBody
    public Double saldo() {
        return getUsuario().getSaldo();
    }

    @PostMapping("/apostar")
    @ResponseBody
    public String apostar(@RequestParam Double valor, @RequestParam Double multiplicador) {
        Usuario u = getUsuario();
        
        if (valor <= 0) {
            return "{\"erro\": \"Valor inválido!\"}";
        }
        
        if(u.getSaldo() >= valor) {
            u.setSaldo(u.getSaldo() - valor);
            u.setSaldo(u.getSaldo() + (valor * multiplicador));
            repository.save(u);
            return "{\"saldo\": " + u.getSaldo() + "}";
        } else {
            return "{\"erro\": \"Saldo insuficiente!\"}";
        }
    }
}