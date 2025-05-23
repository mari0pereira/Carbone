package com.example.bike.model;

import androidx.room.*;
import androidx.room.TypeConverters;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.UUID;

@Entity(tableName = "usuarios")
@TypeConverters({Conversor.class})
public class Usuario {
    @PrimaryKey
    @NotNull

    private String id; // uuid no Supabase
    private String authUid; // uuid no Supabase, vindo da autenticação
    private String nome;
    private String email;
    private String senha;  // ATENÇÃO: Não deve ser armazenado na tabela, apenas para autenticação!!!!
    private String telefone;
    private LocalDate dt_nascimento;
    private String tipoUsuario;

    // Construtor
    public Usuario(String nome, String email, String senha, String telefone,
                   LocalDate dt_nascimento, String tipoUsuario) {
        this.id = UUID.randomUUID().toString();
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.telefone = telefone;
        this.dt_nascimento = dt_nascimento;
        this.tipoUsuario = tipoUsuario;
    }

    // Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthUid() {
        return authUid;
    }

    public void setAuthUid(String authUid) {
        this.authUid = authUid;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public LocalDate getDt_nascimento() {
        return dt_nascimento;
    }

    public void setDt_nascimento(LocalDate dt_nascimento) {
        this.dt_nascimento = dt_nascimento;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }
}
