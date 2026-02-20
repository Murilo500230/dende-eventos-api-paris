package br.com.softhouse.dende.controllers;

import br.com.dende.softhouse.annotations.Controller;
import br.com.dende.softhouse.annotations.request.PostMapping;
import br.com.dende.softhouse.annotations.request.RequestBody;
import br.com.dende.softhouse.annotations.request.RequestMapping;
import br.com.dende.softhouse.process.route.ResponseEntity;
import br.com.softhouse.dende.model.Organizador;
import br.com.softhouse.dende.repositories.Repositorio;

@Controller
@RequestMapping(path = "/organizadores")
public class OrganizadorController {

    private final Repositorio repositorio;

    public OrganizadorController() {
        this.repositorio = Repositorio.getInstance();
    }

    @PostMapping
    public ResponseEntity<String> cadastroOrganizador(@RequestBody Organizador organizador){
        try {
            repositorio.salvarOrganizador(organizador);
            return ResponseEntity.ok("Organizador " + organizador.getEmail() + " registrado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.ok("ERRO: " + e.getMessage());
        }
    }
}