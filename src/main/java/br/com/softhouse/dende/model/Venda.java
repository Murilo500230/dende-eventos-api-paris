package br.com.softhouse.dende.model;

import java.time.LocalDateTime;

public class Venda {
    private Long id;
    private String emailUsuario;
    private Long ingressoId;
    private LocalDateTime dataCompra;
    private double valorPago;

    public Venda() {
        this.dataCompra = LocalDateTime.now();
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmailUsuario() { return emailUsuario; }
    public void setEmailUsuario(String emailUsuario) { this.emailUsuario = emailUsuario; }
    public Long getIngressoId() { return ingressoId; }
    public void setIngressoId(Long ingressoId) { this.ingressoId = ingressoId; }
    public double getValorPago() { return valorPago; }
    public void setValorPago(double valorPago) { this.valorPago = valorPago; }
}