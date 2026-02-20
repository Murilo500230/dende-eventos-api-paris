package br.com.softhouse.dende.model;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

public class Usuario {

    private String nome;
    private LocalDate dataNascimento;
    private String sexo;
    private String email;
    private String senha;
    private boolean ativo = true;

    public Usuario(
            final String nome,
            final LocalDate dataNascimento,
            final String sexo,
            final String email,
            final String senha
    ) {
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.sexo = sexo;
        this.email = email;
        this.senha = senha;
        this.ativo = true;
    }

    public Usuario() {
    }

    // Atende a US 4: Cálculo detalhado da idade
    public String getIdadeFormatada() {
        if (this.dataNascimento == null) return "Data não informada";
        Period periodo = Period.between(this.dataNascimento, LocalDate.now());
        return periodo.getYears() + " anos, " + periodo.getMonths() + " meses e " + periodo.getDays() + " dias";
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }

    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Usuario usuario = (Usuario) object;
        return Objects.equals(email, usuario.email); // E-mail como chave única (US 1)
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "nome='" + nome + '\'' +
                ", idade=" + getIdadeFormatada() + // Mostra idade detalhada no log/perfil (US 4)
                ", email='" + email + '\'' +
                ", ativo=" + ativo +
                '}';
    }
}