package com.projeto.copanheiro.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.projeto.copanheiro.models.JogoPersonalizado;
import com.projeto.copanheiro.models.Usuario;
import com.projeto.copanheiro.repositories.JogoPersonalizadoRepository;
import com.projeto.copanheiro.repositories.UsuarioRepository;
import com.projeto.copanheiro.services.UsuarioService;
import java.util.Random;

@Controller
@RequestMapping("/jogos-personalizados")
public class JogoPersonalizadoController {

    @Autowired
    private JogoPersonalizadoRepository jogoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private UsuarioService usuarioService;
    
    private Random random = new Random();

    // Listar todos os jogos
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("jogos", jogoRepository.findAll());
        model.addAttribute("usuario", usuarioService.getUsuario());
        return "jogos-personalizados/lista";
    }

    // Formulário para criar novo jogo
    @GetMapping("/novo")
    public String novoForm(Model model) {
        model.addAttribute("jogo", new JogoPersonalizado());
        model.addAttribute("usuario", usuarioService.getUsuario());
        return "jogos-personalizados/form";
    }

    // Salvar jogo
    @PostMapping("/salvar")
    public String salvar(@ModelAttribute JogoPersonalizado jogo) {
        jogoRepository.save(jogo);
        return "redirect:/jogos-personalizados";
    }

    // Excluir jogo
    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        jogoRepository.deleteById(id);
        return "redirect:/jogos-personalizados";
    }

    // Página para jogar
    @GetMapping("/jogar/{id}")
    public String jogar(@PathVariable Long id, Model model) {
        JogoPersonalizado jogo = jogoRepository.findById(id).orElseThrow();
        model.addAttribute("jogo", jogo);
        model.addAttribute("usuario", usuarioService.getUsuario());
        return "jogos-personalizados/jogar";
    }

    // Processar jogada com 5 dinâmicas diferentes
    @PostMapping("/jogar/{id}")
    public String processarJogada(@PathVariable Long id,
                                   @RequestParam double valor,
                                   Model model) {
        Usuario usuario = usuarioService.getUsuario();
        JogoPersonalizado jogo = jogoRepository.findById(id).orElseThrow();

        if (valor <= 0 || valor < jogo.getApostaMinima()) {
            model.addAttribute("erro", "Valor inválido! A aposta mínima é R$ " + jogo.getApostaMinima());
        } else if (valor > usuario.getSaldo()) {
            model.addAttribute("erro", "Saldo insuficiente!");
        } else {
            double multiplicadorSorteado = 0;
            double ganho = 0;
            String resultadoMsg = "";
            
            // 5 DINÂMICAS DIFERENTES
            switch (jogo.getTipoDinamica()) {
                case "aviao":
                    // Dinâmica do Avião (sorteia multiplicador, se for maior que 1.5 ganha)
                    multiplicadorSorteado = jogo.getMultiplicadorMin() +
                            (random.nextDouble() * (jogo.getMultiplicadorMax() - jogo.getMultiplicadorMin()));
                    multiplicadorSorteado = Math.round(multiplicadorSorteado * 10) / 10.0;
                    
                    if (multiplicadorSorteado > 1.5) {
                        ganho = valor * multiplicadorSorteado;
                        usuario.setSaldo(usuario.getSaldo() + ganho);
                        resultadoMsg = "✈️ AVIÃO DECOLOU! Ganhou R$" + String.format("%.2f", ganho) +
                                " (Multiplicador: " + multiplicadorSorteado + "x)";
                    } else {
                        usuario.setSaldo(usuario.getSaldo() - valor);
                        resultadoMsg = "💥 AVIÃO CAIU! Perdeu R$" + String.format("%.2f", valor) +
                                " (Multiplicador: " + multiplicadorSorteado + "x)";
                    }
                    break;
                    
                case "roleta":
                    // Dinâmica da Roleta (sorteia número, acertar ganha 5x)
                    int numeroEscolhido = random.nextInt(10);
                    int numeroSorteado = random.nextInt(10);
                    
                    if (numeroEscolhido == numeroSorteado) {
                        ganho = valor * 5;
                        usuario.setSaldo(usuario.getSaldo() + ganho);
                        resultadoMsg = "🎯 ACERTOU! Número " + numeroSorteado + "! Ganhou R$" + String.format("%.2f", ganho);
                    } else {
                        usuario.setSaldo(usuario.getSaldo() - valor);
                        resultadoMsg = "❌ ERROU! Número sorteado: " + numeroSorteado + ". Perdeu R$" + String.format("%.2f", valor);
                    }
                    break;
                    
                case "cassino":
                    // Dinâmica do Caça-níquel
                    int n1 = random.nextInt(5) + 1;
                    int n2 = random.nextInt(5) + 1;
                    int n3 = random.nextInt(5) + 1;
                    
                    if (n1 == n2 && n2 == n3) {
                        ganho = valor * 10;
                        usuario.setSaldo(usuario.getSaldo() + ganho);
                        resultadoMsg = "🎰 JACKPOT! " + n1 + " | " + n2 + " | " + n3 + "! Ganhou R$" + String.format("%.2f", ganho);
                    } else if (n1 == n2 || n1 == n3 || n2 == n3) {
                        ganho = valor * 3;
                        usuario.setSaldo(usuario.getSaldo() + ganho);
                        resultadoMsg = "🎰 PARABÉNS! " + n1 + " | " + n2 + " | " + n3 + "! Ganhou R$" + String.format("%.2f", ganho);
                    } else {
                        usuario.setSaldo(usuario.getSaldo() - valor);
                        resultadoMsg = "🎰 QUE PENA! " + n1 + " | " + n2 + " | " + n3 + "! Perdeu R$" + String.format("%.2f", valor);
                    }
                    break;
                    
                case "caraOuCoroa":
                    // Dinâmica Cara ou Coroa
                    String escolha = random.nextBoolean() ? "CARA" : "COROA";
                    String resultado = random.nextBoolean() ? "CARA" : "COROA";
                    
                    if (escolha.equals(resultado)) {
                        ganho = valor * 2;
                        usuario.setSaldo(usuario.getSaldo() + ganho);
                        resultadoMsg = "🪙 ACERTOU! Deu " + resultado + "! Ganhou R$" + String.format("%.2f", ganho);
                    } else {
                        usuario.setSaldo(usuario.getSaldo() - valor);
                        resultadoMsg = "🪙 ERROU! Deu " + resultado + "! Perdeu R$" + String.format("%.2f", valor);
                    }
                    break;
                    
                case "dado":
                    // Dinâmica do Dado (aposta em número)
                    int apostaDado = random.nextInt(6) + 1;
                    int resultadoDado = random.nextInt(6) + 1;
                    
                    if (apostaDado == resultadoDado) {
                        ganho = valor * 4;
                        usuario.setSaldo(usuario.getSaldo() + ganho);
                        resultadoMsg = "🎲 ACERTOU! Dado: " + resultadoDado + "! Ganhou R$" + String.format("%.2f", ganho);
                    } else {
                        usuario.setSaldo(usuario.getSaldo() - valor);
                        resultadoMsg = "🎲 ERROU! Dado: " + resultadoDado + "! Perdeu R$" + String.format("%.2f", valor);
                    }
                    break;
                    
                default:
                    // Dinâmica padrão (sorteia multiplicador)
                    multiplicadorSorteado = jogo.getMultiplicadorMin() +
                            (random.nextDouble() * (jogo.getMultiplicadorMax() - jogo.getMultiplicadorMin()));
                    multiplicadorSorteado = Math.round(multiplicadorSorteado * 10) / 10.0;
                    
                    if (random.nextBoolean()) {
                        ganho = valor * multiplicadorSorteado;
                        usuario.setSaldo(usuario.getSaldo() + ganho);
                        resultadoMsg = "🎉 GANHOU! R$" + String.format("%.2f", ganho) + " (x" + multiplicadorSorteado + ")";
                    } else {
                        usuario.setSaldo(usuario.getSaldo() - valor);
                        resultadoMsg = "💥 PERDEU! R$" + String.format("%.2f", valor);
                    }
            }
            
            usuarioRepository.save(usuario);
            model.addAttribute("resultado", resultadoMsg);
        }
        
        model.addAttribute("jogo", jogo);
        model.addAttribute("usuario", usuario);
        return "jogos-personalizados/jogar";
    }
}