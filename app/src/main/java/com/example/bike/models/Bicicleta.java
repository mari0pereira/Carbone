package com.example.bike.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "bicicletas")
public class Bicicleta {
    @PrimaryKey(autoGenerate = true)

    private int id;

    private String nome;
    private int modelo; // Ex: 2025, 3
    private double preco;
    private int imageUrl;   // URL da imagem da bike ou (R.drawable.*)
    private String cor;
    private String tamanho; // Tamanho do quadro (P, M, G)
    private String descricao;  // Descrição para o catálogo
    private String especificacoes;// Especificações técnicas da bicicleta
    private String categoria; // Ex: Mountain Bike, Gravel

    // Construtor
    public Bicicleta(String nome, int modelo, double preco, int imageUrl, String cor,
                     String tamanho, String descricao, String especificacoes, String categoria) {
        this.nome = nome;
        this.modelo = modelo;
        this.preco = preco;
        this.imageUrl = imageUrl;
        this.cor = cor;
        this.tamanho = tamanho;
        this.descricao = descricao;
        this.especificacoes = especificacoes;
        this.categoria = categoria;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getModelo() {
        return modelo;
    }

    public void setModelo(int modelo) {
        this.modelo = modelo;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(int imageUrl) {
        this.imageUrl = imageUrl;
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
}