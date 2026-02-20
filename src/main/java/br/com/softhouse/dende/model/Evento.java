package br.com.softhouse.dende.model;

import java.time.LocalDateTime;
import java.time.Duration; // Necessário para calcular a duração (US 7)

public class Evento {
    private Long id;
    private String nome;
    private String descricao;
    private String paginaEvento; // US 7
    private LocalDateTime dataHora; // Representa a Data de Início
    private LocalDateTime dataHoraFim; // US 7
    private Localidade localidade; // US 7
    private String categoria; // SOCIAL, CORPORATIVO, etc.
    private String modalidade; // PRESENCIAL, REMOTO ou HÍBRIDO
    private int capacidadeMaxima; // US 7
    private double precoIngresso; // US 7
    private boolean ativo = false; // Status para US 9 e 10
    private String emailOrganizador; 

    public Evento() {}

    // Lógica de Validação da US 7
    public boolean isValido() {
        LocalDateTime agora = LocalDateTime.now();
        
        // Início não pode ser anterior à data corrente
        if (dataHora == null || dataHora.isBefore(agora)) return false;
        
        // Finalização não pode ser anterior ao início nem à data corrente
        if (dataHoraFim == null || dataHoraFim.isBefore(dataHora) || dataHoraFim.isBefore(agora)) return false;
        
        // Duração mínima de 30 minutos
        long minutos = Duration.between(dataHora, dataHoraFim).toMinutes();
        return minutos >= 30;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getPaginaEvento() { return paginaEvento; }
    public void setPaginaEvento(String paginaEvento) { this.paginaEvento = paginaEvento; }
    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
    public LocalDateTime getDataHoraFim() { return dataHoraFim; }
    public void setDataHoraFim(LocalDateTime dataHoraFim) { this.dataHoraFim = dataHoraFim; }
    public Localidade getLocalidade() { return localidade; }
    public void setLocalidade(Localidade localidade) { this.localidade = localidade; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public String getModalidade() { return modalidade; }
    public void setModalidade(String modalidade) { this.modalidade = modalidade; }
    public int getCapacidadeMaxima() { return capacidadeMaxima; }
    public void setCapacidadeMaxima(int capacidadeMaxima) { this.capacidadeMaxima = capacidadeMaxima; }
    public double getPrecoIngresso() { return precoIngresso; }
    public void setPrecoIngresso(double precoIngresso) { this.precoIngresso = precoIngresso; }
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
    public String getEmailOrganizador() { return emailOrganizador; }
    public void setEmailOrganizador(String emailOrganizador) { this.emailOrganizador = emailOrganizador; }
}