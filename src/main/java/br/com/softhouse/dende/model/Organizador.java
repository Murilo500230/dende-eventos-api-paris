package br.com.softhouse.dende.model;

import java.time.LocalDate;

// Adicionamos 'extends Usuario' para herdar Nome, Email, Senha e Idade
public class Organizador extends Usuario {
    
    // Em vez de repetir CNPJ aqui, usamos a classe Empresa (Composição)
    private Empresa empresa;

    public Organizador() {
        super();
    }

    public Organizador(String nome, LocalDate dataNasc, String sexo, String email, String senha, Empresa empresa) {
        // O super manda os dados para o pai (Usuario)
        super(nome, dataNasc, sexo, email, senha);
        this.empresa = empresa;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    @Override
    public String toString() {
        return "Organizador{" +
                "nome='" + getNome() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", empresa=" + (empresa != null ? empresa.getNomeFantasia() : "Pessoa Física") +
                '}';
    }
}