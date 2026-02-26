package com.projeto.copanheiro.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.projeto.copanheiro.models.Usuario;
import com.projeto.copanheiro.repositories.UsuarioRepository;
import com.projeto.copanheiro.services.UsuarioService;

@Controller
public class HomeController {

    @Autowired
    private UsuarioRepository repository;
    
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/")
    public String home(Model model) {
        Usuario usuario = usuarioService.getUsuario();
        System.out.println("🏠 HomeController - Saldo: " + usuario.getSaldo());
        model.addAttribute("usuario", usuario);
        return "home";
    }

    @GetMapping("/saldo")
    @ResponseBody
    public Double saldo() {
        Double saldo = usuarioService.getUsuario().getSaldo();
        System.out.println("📊 API /saldo - Retornando: " + saldo);
        return saldo;
    }

    @PostMapping("/apostar")
    @ResponseBody
    public String apostar(@RequestParam Double valor, @RequestParam Double multiplicador) {
        Usuario u = usuarioService.getUsuario();
        System.out.println("💰 API /apostar - Antes: " + u.getSaldo());
        
        if (valor <= 0) {
            return "{\"erro\": \"Valor inválido!\"}";
        }
        
        if (u.getSaldo() >= valor) {
            double novoSaldo = u.getSaldo() - valor + (valor * multiplicador);
            u.setSaldo(novoSaldo);
            repository.save(u);
            System.out.println("💰 API /apostar - Depois: " + u.getSaldo());
            return "{\"saldo\": " + u.getSaldo() + "}";
        } else {
            return "{\"erro\": \"Saldo insuficiente!\"}";
        }
    }

    @PostMapping("/reset-saldo")
    @ResponseBody
    public String resetSaldo() {
        Usuario u = usuarioService.getUsuario();
        System.out.println("➕ API /reset-saldo - Antes: " + u.getSaldo());
        
        double novoSaldo = u.getSaldo() + 1000.0;
        u.setSaldo(novoSaldo);
        repository.save(u);
        
        System.out.println("➕ API /reset-saldo - Depois: " + u.getSaldo());
        return "{\"saldo\": " + novoSaldo + "}";
    }
}