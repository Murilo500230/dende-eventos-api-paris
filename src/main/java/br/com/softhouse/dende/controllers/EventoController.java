package br.com.softhouse.dende.controllers;

import br.com.dende.softhouse.annotations.Controller;
import br.com.dende.softhouse.annotations.request.GetMapping;
import br.com.dende.softhouse.annotations.request.PostMapping;
import br.com.dende.softhouse.annotations.request.PutMapping;
import br.com.dende.softhouse.annotations.request.RequestBody;
import br.com.dende.softhouse.annotations.request.RequestMapping;
import br.com.dende.softhouse.annotations.request.PathVariable;
import br.com.dende.softhouse.process.route.ResponseEntity;
import br.com.softhouse.dende.model.Evento;
import br.com.softhouse.dende.repositories.Repositorio;
import java.util.Collection;

@Controller
@RequestMapping(path = "/eventos")
public class EventoController {

    private final Repositorio repositorio;

    public EventoController() {
        this.repositorio = Repositorio.getInstance();
    }

    // US 7: Criar Evento
    @PostMapping
    public ResponseEntity<String> criarEvento(@RequestBody Evento evento) {
        repositorio.salvarEvento(evento);
        return ResponseEntity.ok("Evento '" + evento.getNome() + "' criado com sucesso!");
    }

    // US 9: Listar todos os Eventos
    @GetMapping
    public ResponseEntity<Collection<Evento>> listarEventos() {
        return ResponseEntity.ok(repositorio.listarTodosEventos());
    }

    // US 10: Consultar detalhes de um Evento
    @GetMapping(path = "/{id}")
    public ResponseEntity<Object> consultarEvento(@PathVariable(parameter = "id") String id) {
        try {
            Long idLong = Long.parseLong(id);
            Evento evento = repositorio.buscarEventoPorId(idLong);
            if (evento == null) {
                return ResponseEntity.ok("Evento não encontrado");
            }
            return ResponseEntity.ok(evento);
        } catch (NumberFormatException e) {
            return ResponseEntity.ok("ID inválido");
        }
    }
}