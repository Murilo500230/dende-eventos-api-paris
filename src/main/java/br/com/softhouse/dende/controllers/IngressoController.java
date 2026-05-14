package br.com.softhouse.dende.controllers;

import br.com.dende.softhouse.annotations.Controller;
import br.com.dende.softhouse.annotations.request.*;
import br.com.dende.softhouse.process.route.ResponseEntity;
import br.com.softhouse.dende.exceptions.EventoNaoEncontradoException;
import br.com.softhouse.dende.model.Ingresso;
import br.com.softhouse.dende.model.Venda;
import br.com.softhouse.dende.repositories.IngressoRepository;
import br.com.softhouse.dende.repositories.EventoRepository;
import br.com.softhouse.dende.repositories.VendaRepository;
import br.com.softhouse.dende.repositories.util.ConfigProperties;
import java.util.Collection;

@Controller
@RequestMapping(path = "/ingressos")
public class IngressoController {

    private final IngressoRepository ingressoRepository;
    private final EventoRepository eventoRepository;
    private final VendaRepository vendaRepository;

    public IngressoController() {
        ConfigProperties props = new ConfigProperties();
        this.ingressoRepository = new IngressoRepository(props);
        this.eventoRepository = new EventoRepository(props);
        this.vendaRepository = new VendaRepository(props);
    }

    @PostMapping
    public ResponseEntity<String> criarIngresso(@RequestBody Ingresso ingresso) {
        try {
            eventoRepository.buscarPorId(ingresso.getEventoId());
            ingressoRepository.salvar(ingresso);
            return ResponseEntity.ok("Ingresso do tipo " + ingresso.getTipo() + " criado com sucesso!");
        } catch (EventoNaoEncontradoException e) {
            return ResponseEntity.ok("Erro: Evento não encontrado para este ingresso.");
        }
    }

    @GetMapping(path = "/evento/{eventoId}")
    public ResponseEntity<Collection<Ingresso>> listarPorEvento(@PathVariable(parameter = "eventoId") String eventoId) {
        Long id = Long.parseLong(eventoId);
        return ResponseEntity.ok(ingressoRepository.listarPorEvento(id));
    }

    @PostMapping(path = "/comprar")
    public ResponseEntity<String> comprarIngresso(@RequestBody Venda venda) {
        vendaRepository.salvar(venda);
        return ResponseEntity.ok("Compra realizada com sucesso para o usuario: " + venda.getEmailUsuario());
    }
}
