package com.example.bike.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

public class Bicicleta implements Serializable {
    private int id;
    private String nome;
    private String modelo;
    private double preco;
    private String cor;
    private String tamanho;
    private String descricao;
    private String especificacoes;
    private String categoria;
    private String imageUrl;

    // Construtor
    public Bicicleta(int id, String nome, String modelo, double preco, String cor,
                     String tamanho, String descricao, String especificacoes,
                     String categoria, String imageUrl) {
        this.id = id;
        this.nome = nome;
        this.modelo = modelo;
        this.preco = preco;
        this.cor = cor;
        this.tamanho = tamanho;
        this.descricao = descricao;
        this.especificacoes = especificacoes;
        this.categoria = categoria;
        this.imageUrl = imageUrl;
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

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
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
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return nome + " - " + modelo;
    }
}