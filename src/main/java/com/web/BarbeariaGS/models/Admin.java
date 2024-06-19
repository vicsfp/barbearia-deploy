package com.web.BarbeariaGS.models;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "administradores")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
     private int id;

     @Column(name = "nome", length = 100, nullable = false)
     private String nome;
 
     @Column(name = "email", length = 256, nullable = false)
     private String email;
 
     @Column(name = "senha", nullable = false)
     private String senha;

     @OneToMany(mappedBy = "adminCriacao", cascade = CascadeType.ALL)
    private List<Funcionario> funcionariosCriados;

    @OneToMany(mappedBy = "adminEdicao", cascade = CascadeType.ALL)
    private List<Funcionario> funcionariosEditados;
    
     public List<Funcionario> getFuncionariosCriados() {
        return funcionariosCriados;
    }

    public void setFuncionariosCriados(List<Funcionario> funcionariosCriados) {
        this.funcionariosCriados = funcionariosCriados;
    }

    public List<Funcionario> getFuncionariosEditados() {
        return funcionariosEditados;
    }

    public void setFuncionarios(List<Funcionario> funcionariosEditados) {
        this.funcionariosEditados = funcionariosEditados;
    }

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

    @Override
    public String toString() {
        return nome; // Supondo que o nome seja o atributo que deseja exibir
    }
 }