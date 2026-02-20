package br.com.softhouse.dende.model;

import java.time.LocalDate;


public class Organizador extends Usuario {
    
    private Empresa empresa;

    public Organizador() {
        super();
    }

    public Organizador(String nome, LocalDate dataNasc, String sexo, String email, String senha, Empresa empresa) {
        
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