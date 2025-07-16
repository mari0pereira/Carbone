package com.example.bike.model;

import java.math.BigDecimal;

public class Bicicleta {
    private String id;
    private String nome;
    private String modelo;
    private BigDecimal preco;
    private String cor;
    private String tamanho;
    private String descricao;
    private String especificacoes;
    private String categoria;
    private String imagem;

    // Construtor
    public Bicicleta(String nome, String modelo, BigDecimal preco, String cor, String tamanho,
                     String descricao, String especificacoes, String categoria, String imagem) {
        this.nome = nome;
        this.modelo = modelo;
        this.preco = preco;
        this.cor = cor;
        this.tamanho = tamanho;
        this.descricao = descricao;
        this.especificacoes = especificacoes;
        this.categoria = categoria;
        this.imagem = imagem;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getTamanho() {
        return tamanho;
    }

    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getEspecificacoes() {
        return especificacoes;
    }

    public void setEspecificacoes(String especificacoes) {
        this.especificacoes = especificacoes;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getImageUrl() {
        return imagem;
    }

    public void setImageUrl(String imageUrl) {
        this.imagem = imageUrl;
    }
}