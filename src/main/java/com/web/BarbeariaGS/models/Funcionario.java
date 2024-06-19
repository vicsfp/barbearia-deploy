package com.web.BarbeariaGS.models;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "funcionarios")
public class Funcionario {
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
     private int id;

     @Column(name = "nome", length = 100, nullable = false)
     private String nome;

     @Column(name = "cargo", length = 100, nullable = false)
     private String cargo;
 
     @Column(name = "email", length = 256, nullable = false)
     private String email;
 
     @Column(name = "senha", nullable = false)
     private String senha;

    @ManyToOne
    @JoinColumn(name = "admin_id_criacao", referencedColumnName = "id")
    private Admin adminCriacao;

    @ManyToOne
    @JoinColumn(name = "admin_id_edicao", referencedColumnName = "id")
    private Admin adminEdicao;

    @OneToMany(mappedBy = "funcionario", cascade = CascadeType.ALL)
    private List<Agendamento> agendamentos;
    
     public List<Agendamento> getAgendamentos() {
        return agendamentos;
    }

    public void setAgendamentos(List<Agendamento> agendamentos) {
        this.agendamentos = agendamentos;
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

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
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

    public Admin getAdminCriacao() {
        return adminCriacao;
    }
    
    public void setAdminCriacao(Admin adminCriacao) {
        this.adminCriacao = adminCriacao;
    }

    public Admin getAdminEdicao() {
        return adminEdicao;
    }
    
    public void setAdminEdicao(Admin adminEdicao) {
        this.adminEdicao = adminEdicao;
    }

    
}
