package com.projeto.copanheiro.models;

import jakarta.persistence.*;

@Entity
public class JogoPersonalizado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String descricao;
    private String tipoDinamica; // "aviao", "roleta", "cassino", "caraOuCoroa", "dado"
    private Double multiplicadorMin;
    private Double multiplicadorMax;
    private Double apostaMinima;
    private String corFundo;
    private String icone;

    // Construtores
    public JogoPersonalizado() {}

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getTipoDinamica() { return tipoDinamica; }
    public void setTipoDinamica(String tipoDinamica) { this.tipoDinamica = tipoDinamica; }

    public Double getMultiplicadorMin() { return multiplicadorMin; }
    public void setMultiplicadorMin(Double multiplicadorMin) { this.multiplicadorMin = multiplicadorMin; }

    public Double getMultiplicadorMax() { return multiplicadorMax; }
    public void setMultiplicadorMax(Double multiplicadorMax) { this.multiplicadorMax = multiplicadorMax; }

    public Double getApostaMinima() { return apostaMinima; }
    public void setApostaMinima(Double apostaMinima) { this.apostaMinima = apostaMinima; }

    public String getCorFundo() { return corFundo; }
    public void setCorFundo(String corFundo) { this.corFundo = corFundo; }

    public String getIcone() { return icone; }
    public void setIcone(String icone) { this.icone = icone; }
}