package com.projeto.copanheiro.models;

import jakarta.persistence.*;

@Entity
public class TipoJogo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nome;
    private String descricao;
    private Double multiplicadorMin;
    private Double multiplicadorMax;

    // Construtores
    public TipoJogo() {}
    
    public TipoJogo(String nome, String descricao, Double multiplicadorMin, Double multiplicadorMax) {
        this.nome = nome;
        this.descricao = descricao;
        this.multiplicadorMin = multiplicadorMin;
        this.multiplicadorMax = multiplicadorMax;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Double getMultiplicadorMin() { return multiplicadorMin; }
    public void setMultiplicadorMin(Double multiplicadorMin) { this.multiplicadorMin = multiplicadorMin; }

    public Double getMultiplicadorMax() { return multiplicadorMax; }
    public void setMultiplicadorMax(Double multiplicadorMax) { this.multiplicadorMax = multiplicadorMax; }
}