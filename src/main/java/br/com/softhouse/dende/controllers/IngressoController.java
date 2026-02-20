package br.com.softhouse.dende.controllers;

import br.com.dende.softhouse.annotations.Controller;
import br.com.dende.softhouse.annotations.request.*;
import br.com.dende.softhouse.process.route.ResponseEntity;
import br.com.softhouse.dende.model.Ingresso;
import br.com.softhouse.dende.model.Venda;
import br.com.softhouse.dende.repositories.Repositorio;
import java.util.Collection;

@Controller
@RequestMapping(path = "/ingressos")
public class IngressoController {

    private final Repositorio repositorio;

    public IngressoController() {
        this.repositorio = Repositorio.getInstance();
    }

    // US 12: Criar Ingresso para um Evento
    @PostMapping
    public ResponseEntity<String> criarIngresso(@RequestBody Ingresso ingresso) {
        if (repositorio.buscarEventoPorId(ingresso.getEventoId()) == null) {
            return ResponseEntity.ok("Erro: Evento não encontrado para este ingresso.");
        }
        repositorio.salvarIngresso(ingresso);
        return ResponseEntity.ok("Ingresso do tipo " + ingresso.getTipo() + " criado com sucesso!");
    }

    // US 13: Listar Ingressos de um Evento específico
    @GetMapping(path = "/evento/{eventoId}")
    public ResponseEntity<Collection<Ingresso>> listarPorEvento(@PathVariable(parameter = "eventoId") String eventoId) {
        Long id = Long.parseLong(eventoId);
        return ResponseEntity.ok(repositorio.listarIngressosPorEvento(id));
    }

    // US 14 e 15: Comprar Ingresso (Gerar Venda)
    @PostMapping(path = "/comprar")
    public ResponseEntity<String> comprarIngresso(@RequestBody Venda venda) {
        // Aqui a lógica simplificada para o trabalho:
        repositorio.salvarVenda(venda);
        return ResponseEntity.ok("Compra realizada com sucesso para o usuario: " + venda.getEmailUsuario());
    }
}