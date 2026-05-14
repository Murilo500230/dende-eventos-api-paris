package br.com.softhouse.dende.repositories;

import br.com.softhouse.dende.exceptions.DatabaseException;
import br.com.softhouse.dende.exceptions.UsuarioNaoEncontradoException;
import br.com.softhouse.dende.model.Organizador;
import br.com.softhouse.dende.model.Empresa;
import br.com.softhouse.dende.repositories.util.ConnectionPool;
import br.com.softhouse.dende.repositories.util.ConfigProperties;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class OrganizadorRepository {

    private final DataSource dataSource;

    public OrganizadorRepository(ConfigProperties props) {
        this.dataSource = ConnectionPool.getDataSource(props);
    }

    public void salvar(Organizador organizador) {
        String sql = "INSERT INTO usuario (nome, data_nascimento, sexo, email, senha, ativo, tipo) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, organizador.getNome());
            stmt.setDate(2, Date.valueOf(organizador.getDataNascimento()));
            stmt.setString(3, organizador.getSexo());
            stmt.setString(4, organizador.getEmail());
            stmt.setString(5, organizador.getSenha());
            stmt.setBoolean(6, organizador.isAtivo());
            stmt.setString(7, "ORGANIZADOR");
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao salvar organizador", e);
        }
    }

    public Organizador buscarPorEmail(String email) {
        String sql = "SELECT * FROM usuario WHERE email = ? AND tipo = 'ORGANIZADOR'";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapearOrganizador(rs);
            }
            throw new UsuarioNaoEncontradoException("Organizador com email " + email + " não encontrado");
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar organizador", e);
        }
    }

    public Collection<Organizador> listarTodos() {
        String sql = "SELECT * FROM usuario WHERE tipo = 'ORGANIZADOR'";
        Collection<Organizador> organizadores = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                organizadores.add(mapearOrganizador(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao listar organizadores", e);
        }
        return organizadores;
    }

    private Organizador mapearOrganizador(ResultSet rs) throws SQLException {
        Organizador organizador = new Organizador();
        organizador.setNome(rs.getString("nome"));
        organizador.setDataNascimento(rs.getDate("data_nascimento").toLocalDate());
        organizador.setSexo(rs.getString("sexo"));
        organizador.setEmail(rs.getString("email"));
        organizador.setSenha(rs.getString("senha"));
        organizador.setAtivo(rs.getBoolean("ativo"));
        return organizador;
    }
}
