package br.com.softhouse.dende.repositories;

import br.com.softhouse.dende.exceptions.DatabaseException;
import br.com.softhouse.dende.exceptions.UsuarioNaoEncontradoException;
import br.com.softhouse.dende.model.Usuario;
import br.com.softhouse.dende.repositories.util.ConnectionPool;
import br.com.softhouse.dende.repositories.util.ConfigProperties;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class UsuarioRepository {

    private final DataSource dataSource;

    public UsuarioRepository(ConfigProperties props) {
        this.dataSource = ConnectionPool.getDataSource(props);
    }

    public void salvar(Usuario usuario) {
        String sql = "INSERT INTO usuario (nome, data_nascimento, sexo, email, senha, ativo, tipo) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNome());
            stmt.setDate(2, Date.valueOf(usuario.getDataNascimento()));
            stmt.setString(3, usuario.getSexo());
            stmt.setString(4, usuario.getEmail());
            stmt.setString(5, usuario.getSenha());
            stmt.setBoolean(6, usuario.isAtivo());
            stmt.setString(7, "COMUM");
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao salvar usuário", e);
        }
    }

    public Usuario buscarPorEmail(String email) {
        String sql = "SELECT * FROM usuario WHERE email = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapearUsuario(rs);
            }
            throw new UsuarioNaoEncontradoException("Usuário com email " + email + " não encontrado");
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar usuário", e);
        }
    }

    public Collection<Usuario> listarTodos() {
        String sql = "SELECT * FROM usuario";
        Collection<Usuario> usuarios = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                usuarios.add(mapearUsuario(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao listar usuários", e);
        }
        return usuarios;
    }

    public void atualizar(Usuario usuario) {
        String sql = "UPDATE usuario SET nome = ?, data_nascimento = ?, sexo = ?, senha = ?, ativo = ? WHERE email = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNome());
            stmt.setDate(2, Date.valueOf(usuario.getDataNascimento()));
            stmt.setString(3, usuario.getSexo());
            stmt.setString(4, usuario.getSenha());
            stmt.setBoolean(5, usuario.isAtivo());
            stmt.setString(6, usuario.getEmail());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao atualizar usuário", e);
        }
    }

    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setNome(rs.getString("nome"));
        usuario.setDataNascimento(rs.getDate("data_nascimento").toLocalDate());
        usuario.setSexo(rs.getString("sexo"));
        usuario.setEmail(rs.getString("email"));
        usuario.setSenha(rs.getString("senha"));
        usuario.setAtivo(rs.getBoolean("ativo"));
        return usuario;
    }
}
