package br.com.softhouse.dende.repositories;

import br.com.softhouse.dende.exceptions.DatabaseException;
import br.com.softhouse.dende.model.Ingresso;
import br.com.softhouse.dende.repositories.util.ConnectionPool;
import br.com.softhouse.dende.repositories.util.ConfigProperties;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class IngressoRepository {

    private final DataSource dataSource;

    public IngressoRepository(ConfigProperties props) {
        this.dataSource = ConnectionPool.getDataSource(props);
    }

    public void salvar(Ingresso ingresso) {
        String sql = "INSERT INTO ingresso (evento_id, tipo, preco, quantidade_disponivel) VALUES (?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, ingresso.getEventoId());
            stmt.setString(2, ingresso.getTipo());
            stmt.setDouble(3, ingresso.getPreco());
            stmt.setInt(4, ingresso.getQuantidadeDisponivel());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao salvar ingresso", e);
        }
    }

    public Collection<Ingresso> listarPorEvento(Long eventoId) {
        String sql = "SELECT * FROM ingresso WHERE evento_id = ?";
        Collection<Ingresso> ingressos = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, eventoId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ingressos.add(mapearIngresso(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao listar ingressos", e);
        }
        return ingressos;
    }

    private Ingresso mapearIngresso(ResultSet rs) throws SQLException {
        Ingresso ingresso = new Ingresso();
        ingresso.setId(rs.getLong("id"));
        ingresso.setEventoId(rs.getLong("evento_id"));
        ingresso.setTipo(rs.getString("tipo"));
        ingresso.setPreco(rs.getDouble("preco"));
        ingresso.setQuantidadeDisponivel(rs.getInt("quantidade_disponivel"));
        return ingresso;
    }
}
