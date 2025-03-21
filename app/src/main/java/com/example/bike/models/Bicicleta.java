package com.example.bike.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "bicicletas")
public class Bicicleta {
    @PrimaryKey(autoGenerate = true)

    private int id;

    private String nome;
    private int modelo; // Ex: 2025
    private String tamanho; // Tamanho do quadro (P, M, G)
    private String categoria; // Ex: Mountain Bike, Gravel, E-MTB, Urbanas
    private double preco;
    private String imageUrl; // URL da imagem da bike
    private String especificacoes;// Especificações técnicas da bicicleta

    // Construtor
    public Bicicleta(String nome, int modelo, String tamanho, String categoria, double preco,
                     String imageUrl, String especificacoes) {
        this.nome = nome;
        this.modelo = modelo;
        this.tamanho = tamanho;
        this.categoria = categoria;
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

    public int getModelo() {
        return modelo;
    }

    public void setModelo(int modelo) {
        this.modelo = modelo;
    }

    public String getTamanho() {
        return tamanho;
    }

    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
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
