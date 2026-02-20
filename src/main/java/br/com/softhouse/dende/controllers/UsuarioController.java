package br.com.softhouse.dende.controllers;

import br.com.dende.softhouse.annotations.Controller;
import br.com.dende.softhouse.annotations.request.GetMapping;
import br.com.dende.softhouse.annotations.request.PostMapping;
import br.com.dende.softhouse.annotations.request.PutMapping;
import br.com.dende.softhouse.annotations.request.RequestBody;
import br.com.dende.softhouse.annotations.request.RequestMapping;
import br.com.dende.softhouse.annotations.request.PathVariable;
import br.com.dende.softhouse.process.route.ResponseEntity;
import br.com.softhouse.dende.model.Usuario;
import br.com.softhouse.dende.model.Organizador; // Import necessário para a US 5
import br.com.softhouse.dende.repositories.Repositorio;

@Controller
@RequestMapping(path = "/usuarios")
public class UsuarioController {

    private final Repositorio repositorio;

    public UsuarioController() {
        this.repositorio = Repositorio.getInstance();
    }

    @PostMapping
    public ResponseEntity<String> cadastroUsuario(@RequestBody Usuario usuario){
        try {
            repositorio.salvarUsuario(usuario);
            return ResponseEntity.ok("Usuario " + usuario.getEmail() + " registrado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.ok("ERRO: " + e.getMessage());
        }
    }

    @GetMapping(path = "/{email}")
    public ResponseEntity<Object> getUsuario(@PathVariable(parameter = "email") String email) {
        Usuario usuario = repositorio.buscarUsuarioPorEmail(email);
        if (usuario == null) {
            // Tenta buscar como organizador caso não ache no mapa de usuário comum
            usuario = repositorio.buscarOrganizadorPorEmail(email);
        }
        
        if (usuario == null) {
            return ResponseEntity.ok("Usuario não encontrado");
        }
        return ResponseEntity.ok(usuario);
    }

    @PutMapping(path = "/{email}")
    public ResponseEntity<String> alterarUsuario(@PathVariable(parameter = "email") String email, @RequestBody Usuario usuarioDadosNovos) {
        Usuario usuarioExistente = repositorio.buscarUsuarioPorEmail(email);
        if (usuarioExistente == null) {
            usuarioExistente = repositorio.buscarOrganizadorPorEmail(email);
        }

        if (usuarioExistente == null) {
            return ResponseEntity.ok("Usuario inexistente");
        }

        usuarioExistente.setNome(usuarioDadosNovos.getNome());
        usuarioExistente.setDataNascimento(usuarioDadosNovos.getDataNascimento());
        usuarioExistente.setSexo(usuarioDadosNovos.getSexo());
        usuarioExistente.setSenha(usuarioDadosNovos.getSenha());
        return ResponseEntity.ok("Usuario " + email + " alterado com sucesso!");
    }

    // --- US 5: Desativar Conta com Trava de Segurança ---
    @PutMapping(path = "/{email}/desativar")
    public ResponseEntity<String> desativarUsuario(@PathVariable(parameter = "email") String email) {
        // Busca o usuário (pode ser comum ou organizador)
        Usuario usuario = repositorio.buscarUsuarioPorEmail(email);
        if (usuario == null) usuario = repositorio.buscarOrganizadorPorEmail(email);

        if (usuario == null) {
            return ResponseEntity.ok("Usuario inexistente");
        }

        // REGRA DA US 5: Organizador não pode desativar se tiver eventos ativos
        if (usuario instanceof Organizador) {
            boolean temEventosAtivos = repositorio.listarTodosEventos().stream()
                .anyMatch(e -> e.getEmailOrganizador().equals(email) && e.isAtivo());
                
            if (temEventosAtivos) {
                return ResponseEntity.ok("ERRO: Voce possui eventos ativos! Desative ou encerre os eventos antes de desativar sua conta.");
            }
        }

        usuario.setAtivo(false);
        return ResponseEntity.ok("Conta de " + email + " desativada com sucesso!");
    }

    // --- US 6: Reativar Conta ---
    @PutMapping(path = "/{email}/reativar")
    public ResponseEntity<String> reativarUsuario(@PathVariable(parameter = "email") String email) {
        Usuario usuario = repositorio.buscarUsuarioPorEmail(email);
        if (usuario == null) usuario = repositorio.buscarOrganizadorPorEmail(email);

        if (usuario == null) {
            return ResponseEntity.ok("Usuario inexistente");
        }

        usuario.setAtivo(true);
        return ResponseEntity.ok("Conta de " + email + " reativada com sucesso!");
    }
}