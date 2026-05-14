package br.com.softhouse.dende.controllers;

import br.com.dende.softhouse.annotations.Controller;
import br.com.dende.softhouse.annotations.request.GetMapping;
import br.com.dende.softhouse.annotations.request.PostMapping;
import br.com.dende.softhouse.annotations.request.PutMapping;
import br.com.dende.softhouse.annotations.request.RequestBody;
import br.com.dende.softhouse.annotations.request.RequestMapping;
import br.com.dende.softhouse.annotations.request.PathVariable;
import br.com.dende.softhouse.process.route.ResponseEntity;
import br.com.softhouse.dende.exceptions.UsuarioNaoEncontradoException;
import br.com.softhouse.dende.model.Usuario;
import br.com.softhouse.dende.model.Organizador;
import br.com.softhouse.dende.repositories.UsuarioRepository;
import br.com.softhouse.dende.repositories.OrganizadorRepository;
import br.com.softhouse.dende.repositories.EventoRepository;
import br.com.softhouse.dende.repositories.util.ConfigProperties;

@Controller
@RequestMapping(path = "/usuarios")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final OrganizadorRepository organizadorRepository;
    private final EventoRepository eventoRepository;

    public UsuarioController() {
        ConfigProperties props = new ConfigProperties();
        this.usuarioRepository = new UsuarioRepository(props);
        this.organizadorRepository = new OrganizadorRepository(props);
        this.eventoRepository = new EventoRepository(props);
    }

    @PostMapping
    public ResponseEntity<String> cadastroUsuario(@RequestBody Usuario usuario) {
        try {
            usuarioRepository.salvar(usuario);
            return ResponseEntity.ok("Usuario " + usuario.getEmail() + " registrado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.ok("ERRO: " + e.getMessage());
        }
    }

    @GetMapping(path = "/{email}")
    public ResponseEntity<Object> getUsuario(@PathVariable(parameter = "email") String email) {
        try {
            Usuario usuario = usuarioRepository.buscarPorEmail(email);
            return ResponseEntity.ok(usuario);
        } catch (UsuarioNaoEncontradoException e) {
            try {
                Usuario organizador = organizadorRepository.buscarPorEmail(email);
                return ResponseEntity.ok(organizador);
            } catch (UsuarioNaoEncontradoException ex) {
                return ResponseEntity.ok("Usuario não encontrado");
            }
        }
    }

    @PutMapping(path = "/{email}")
    public ResponseEntity<String> alterarUsuario(@PathVariable(parameter = "email") String email, @RequestBody Usuario usuarioDadosNovos) {
        try {
            Usuario usuarioExistente = usuarioRepository.buscarPorEmail(email);
            usuarioExistente.setNome(usuarioDadosNovos.getNome());
            usuarioExistente.setDataNascimento(usuarioDadosNovos.getDataNascimento());
            usuarioExistente.setSexo(usuarioDadosNovos.getSexo());
            usuarioExistente.setSenha(usuarioDadosNovos.getSenha());
            usuarioRepository.atualizar(usuarioExistente);
            return ResponseEntity.ok("Usuario " + email + " alterado com sucesso!");
        } catch (UsuarioNaoEncontradoException e) {
            return ResponseEntity.ok("Usuario inexistente");
        }
    }

    @PutMapping(path = "/{email}/desativar")
    public ResponseEntity<String> desativarUsuario(@PathVariable(parameter = "email") String email) {
        try {
            Usuario usuario = usuarioRepository.buscarPorEmail(email);
            if (usuario instanceof Organizador) {
                boolean temEventosAtivos = eventoRepository.listarTodos().stream()
                    .anyMatch(e -> e.getEmailOrganizador().equals(email) && e.isAtivo());
                if (temEventosAtivos) {
                    return ResponseEntity.ok("ERRO: Voce possui eventos ativos! Desative ou encerre os eventos antes de desativar sua conta.");
                }
            }
            usuario.setAtivo(false);
            usuarioRepository.atualizar(usuario);
            return ResponseEntity.ok("Conta de " + email + " desativada com sucesso!");
        } catch (UsuarioNaoEncontradoException e) {
            return ResponseEntity.ok("Usuario inexistente");
        }
    }

    @PutMapping(path = "/{email}/reativar")
    public ResponseEntity<String> reativarUsuario(@PathVariable(parameter = "email") String email) {
        try {
            Usuario usuario = usuarioRepository.buscarPorEmail(email);
            usuario.setAtivo(true);
            usuarioRepository.atualizar(usuario);
            return ResponseEntity.ok("Conta de " + email + " reativada com sucesso!");
        } catch (UsuarioNaoEncontradoException e) {
            return ResponseEntity.ok("Usuario inexistente");
        }
    }
}
