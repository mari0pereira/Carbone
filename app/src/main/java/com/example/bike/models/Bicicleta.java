package com.example.bike.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "bicicletas")
public class Bicicleta {
    @PrimaryKey(autoGenerate = true)

    private int id;

    private String nome;
    private String descricao;
    private String categoria; // Ex: Mountain Bike, Gravel, E-MTB, Urbanas
    private int modelo; // Ex: 2025
    private double preco;
    private String imageUrl; // URL da imagem da bike
    private String especificacoes; // Especificações técnicas

    // Construtor / Constructor
    public Bicicleta(String nome, String descricao, String categoria, int modelo,
                     double preco, String imageUrl, String especificacoes) {
        // não precisa colocar o ID porque já é gerado automaticamente
        this.nome = nome;
        this.descricao = descricao;
        this.categoria = categoria;
        this.modelo = modelo;
        this.preco = preco;
        this.imageUrl = imageUrl;
        this.especificacoes = especificacoes;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getEspecificacoes() {
        return especificacoes;
    }

    public void setEspecificacoes(String especificacoes) {
        this.especificacoes = especificacoes;
    }
}
