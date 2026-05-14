package br.com.softhouse.dende.controllers;

import br.com.dende.softhouse.annotations.Controller;
import br.com.dende.softhouse.annotations.request.GetMapping;
import br.com.dende.softhouse.annotations.request.PostMapping;
import br.com.dende.softhouse.annotations.request.PutMapping;
import br.com.dende.softhouse.annotations.request.RequestBody;
import br.com.dende.softhouse.annotations.request.RequestMapping;
import br.com.dende.softhouse.annotations.request.PathVariable;
import br.com.dende.softhouse.process.route.ResponseEntity;
import br.com.softhouse.dende.exceptions.EventoNaoEncontradoException;
import br.com.softhouse.dende.model.Evento;
import br.com.softhouse.dende.repositories.EventoRepository;
import br.com.softhouse.dende.repositories.util.ConfigProperties;
import java.util.Collection;

@Controller
@RequestMapping(path = "/eventos")
public class EventoController {

    private final EventoRepository eventoRepository;

    public EventoController() {
        ConfigProperties props = new ConfigProperties();
        this.eventoRepository = new EventoRepository(props);
    }

    @PostMapping
    public ResponseEntity<String> criarEvento(@RequestBody Evento evento) {
        try {
            eventoRepository.salvar(evento);
            return ResponseEntity.ok("Evento '" + evento.getNome() + "' criado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.ok("ERRO: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<Collection<Evento>> listarEventos() {
        return ResponseEntity.ok(eventoRepository.listarTodos());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Object> consultarEvento(@PathVariable(parameter = "id") String id) {
        try {
            Long idLong = Long.parseLong(id);
            Evento evento = eventoRepository.buscarPorId(idLong);
            return ResponseEntity.ok(evento);
        } catch (EventoNaoEncontradoException e) {
            return ResponseEntity.ok("Evento não encontrado");
        } catch (NumberFormatException e) {
            return ResponseEntity.ok("ID inválido");
        }
    }
}
