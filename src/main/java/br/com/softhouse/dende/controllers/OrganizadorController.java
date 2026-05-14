package br.com.softhouse.dende.controllers;

import br.com.dende.softhouse.annotations.Controller;
import br.com.dende.softhouse.annotations.request.PostMapping;
import br.com.dende.softhouse.annotations.request.RequestBody;
import br.com.dende.softhouse.annotations.request.RequestMapping;
import br.com.dende.softhouse.process.route.ResponseEntity;
import br.com.softhouse.dende.model.Organizador;
import br.com.softhouse.dende.repositories.OrganizadorRepository;
import br.com.softhouse.dende.repositories.util.ConfigProperties;

@Controller
@RequestMapping(path = "/organizadores")
public class OrganizadorController {

    private final OrganizadorRepository organizadorRepository;

    public OrganizadorController() {
        ConfigProperties props = new ConfigProperties();
        this.organizadorRepository = new OrganizadorRepository(props);
    }

    @PostMapping
    public ResponseEntity<String> cadastroOrganizador(@RequestBody Organizador organizador) {
        try {
            organizadorRepository.salvar(organizador);
            return ResponseEntity.ok("Organizador " + organizador.getEmail() + " registrado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.ok("ERRO: " + e.getMessage());
        }
    }
}
