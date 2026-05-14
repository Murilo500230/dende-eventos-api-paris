package br.com.softhouse.dende.repositories;

import br.com.softhouse.dende.exceptions.DatabaseException;
import br.com.softhouse.dende.exceptions.EventoNaoEncontradoException;
import br.com.softhouse.dende.model.Evento;
import br.com.softhouse.dende.repositories.util.ConnectionPool;
import br.com.softhouse.dende.repositories.util.ConfigProperties;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class EventoRepository {

    private final DataSource dataSource;

    public EventoRepository(ConfigProperties props) {
        this.dataSource = ConnectionPool.getDataSource(props);
    }

    public void salvar(Evento evento) {
        String sql = "INSERT INTO evento (nome, descricao, pagina_evento, data_hora, data_hora_fim, categoria, modalidade, capacidade_maxima, preco_ingresso, ativo, email_organizador) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, evento.getNome());
            stmt.setString(2, evento.getDescricao());
            stmt.setString(3, evento.getPaginaEvento());
            stmt.setTimestamp(4, Timestamp.valueOf(evento.getDataHora()));
            stmt.setTimestamp(5, Timestamp.valueOf(evento.getDataHoraFim()));
            stmt.setString(6, evento.getCategoria());
            stmt.setString(7, evento.getModalidade());
            stmt.setInt(8, evento.getCapacidadeMaxima());
            stmt.setDouble(9, evento.getPrecoIngresso());
            stmt.setBoolean(10, evento.isAtivo());
            stmt.setString(11, evento.getEmailOrganizador());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao salvar evento", e);
        }
    }

    public Evento buscarPorId(Long id) {
        String sql = "SELECT * FROM evento WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapearEvento(rs);
            }
            throw new EventoNaoEncontradoException("Evento com id " + id + " não encontrado");
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar evento", e);
        }
    }

    public Collection<Evento> listarTodos() {
        String sql = "SELECT * FROM evento";
        Collection<Evento> eventos = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                eventos.add(mapearEvento(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao listar eventos", e);
        }
        return eventos;
    }

    public void atualizar(Evento evento) {
        String sql = "UPDATE evento SET nome = ?, descricao = ?, pagina_evento = ?, data_hora = ?, data_hora_fim = ?, categoria = ?, modalidade = ?, capacidade_maxima = ?, preco_ingresso = ?, ativo = ? WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, evento.getNome());
            stmt.setString(2, evento.getDescricao());
            stmt.setString(3, evento.getPaginaEvento());
            stmt.setTimestamp(4, Timestamp.valueOf(evento.getDataHora()));
            stmt.setTimestamp(5, Timestamp.valueOf(evento.getDataHoraFim()));
            stmt.setString(6, evento.getCategoria());
            stmt.setString(7, evento.getModalidade());
            stmt.setInt(8, evento.getCapacidadeMaxima());
            stmt.setDouble(9, evento.getPrecoIngresso());
            stmt.setBoolean(10, evento.isAtivo());
            stmt.setLong(11, evento.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao atualizar evento", e);
        }
    }

    private Evento mapearEvento(ResultSet rs) throws SQLException {
        Evento evento = new Evento();
        evento.setId(rs.getLong("id"));
        evento.setNome(rs.getString("nome"));
        evento.setDescricao(rs.getString("descricao"));
        evento.setPaginaEvento(rs.getString("pagina_evento"));
        evento.setDataHora(rs.getTimestamp("data_hora").toLocalDateTime());
        evento.setDataHoraFim(rs.getTimestamp("data_hora_fim").toLocalDateTime());
        evento.setCategoria(rs.getString("categoria"));
        evento.setModalidade(rs.getString("modalidade"));
        evento.setCapacidadeMaxima(rs.getInt("capacidade_maxima"));
        evento.setPrecoIngresso(rs.getDouble("preco_ingresso"));
        evento.setAtivo(rs.getBoolean("ativo"));
        evento.setEmailOrganizador(rs.getString("email_organizador"));
        return evento;
    }
}
