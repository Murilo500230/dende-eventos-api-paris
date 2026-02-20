package br.com.softhouse.dende.repositories;

import br.com.softhouse.dende.model.*;
import java.util.*;

public class Repositorio {

    private static Repositorio instance = new Repositorio();
    
    // Mapas para armazenamento em memória
    private final Map<String, Usuario> usuariosComum;
    private final Map<String, Organizador> organizadores;
    private final Map<Long, Evento> eventos;
    private final Map<Long, Ingresso> ingressos;
    private final Map<Long, Venda> vendas;
    
    // Contadores de ID
    private long proximoIdEvento = 1;
    private long proximoIdIngresso = 1;
    private long proximoIdVenda = 1;

    private Repositorio() {
        this.usuariosComum = new HashMap<>();
        this.organizadores = new HashMap<>();
        this.eventos = new HashMap<>();
        this.ingressos = new HashMap<>();
        this.vendas = new HashMap<>();
    }

    public static Repositorio getInstance() {
        return instance;
    }

    // --- MÉTODOS DE USUÁRIO E ORGANIZADOR ---

    public void salvarUsuario(Usuario usuario) {
        validarEmailUnico(usuario.getEmail()); // Atende US 1
        usuariosComum.put(usuario.getEmail(), usuario);
    }

    public Usuario buscarUsuarioPorEmail(String email) {
        return usuariosComum.get(email);
    }

    public void salvarOrganizador(Organizador organizador) {
        validarEmailUnico(organizador.getEmail()); // Atende US 2
        organizadores.put(organizador.getEmail(), organizador);
    }

    public Organizador buscarOrganizadorPorEmail(String email) {
        return organizadores.get(email);
    }

    // Mudança solicitada para validar e-mail único (US 1 e 2)
    private void validarEmailUnico(String email) {
        if (usuariosComum.containsKey(email) || organizadores.containsKey(email)) {
            throw new RuntimeException("ERRO: Este e-mail já está em uso!"); 
        }
    }

    // --- MÉTODOS DE EVENTO ---

    public void salvarEvento(Evento evento) {
        // Validação da US 7 antes de salvar
        if (!evento.isValido()) {
            throw new RuntimeException("ERRO: Evento inválido (verifique datas e duração mínima de 30min).");
        }
        
        if (evento.getId() == null) {
            evento.setId(proximoIdEvento++);
        }
        eventos.put(evento.getId(), evento);
    }

    public Evento buscarEventoPorId(Long id) {
        return eventos.get(id);
    }

    public Collection<Evento> listarTodosEventos() {
        return eventos.values();
    }

    // Ajuste US 10: Se desativar/excluir, tratar ingressos
    public void excluirEvento(Long id) {
        eventos.remove(id);
        // Regra da US 10: Ingressos devem ser cancelados se o evento sair do ar
        ingressos.values().removeIf(i -> i.getEventoId().equals(id));
    }

    // --- MÉTODOS DE INGRESSO ---

    public void salvarIngresso(Ingresso ingresso) {
        if (ingresso.getId() == null) {
            ingresso.setId(proximoIdIngresso++);
        }
        ingressos.put(ingresso.getId(), ingresso);
    }

    public Collection<Ingresso> listarIngressosPorEvento(Long eventoId) {
        List<Ingresso> lista = new ArrayList<>();
        for (Ingresso i : ingressos.values()) {
            if (i.getEventoId().equals(eventoId)) {
                lista.add(i);
            }
        }
        return lista;
    }

    // --- MÉTODOS DE VENDA ---

    public void salvarVenda(Venda venda) {
        if (venda.getId() == null) {
            venda.setId(proximoIdVenda++);
        }
        vendas.put(venda.getId(), venda);
    }
}