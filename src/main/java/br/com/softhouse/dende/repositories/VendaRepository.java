package br.com.softhouse.dende.repositories;

import br.com.softhouse.dende.exceptions.DatabaseException;
import br.com.softhouse.dende.model.Venda;
import br.com.softhouse.dende.repositories.util.ConnectionPool;
import br.com.softhouse.dende.repositories.util.ConfigProperties;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class VendaRepository {

    private final DataSource dataSource;

    public VendaRepository(ConfigProperties props) {
        this.dataSource = ConnectionPool.getDataSource(props);
    }

    public void salvar(Venda venda) {
        String sql = "INSERT INTO venda (email_usuario, ingresso_id, data_compra, valor_pago) VALUES (?, ?, NOW(), ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, venda.getEmailUsuario());
            stmt.setLong(2, venda.getIngressoId());
            stmt.setDouble(3, venda.getValorPago());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao salvar venda", e);
        }
    }

    public Collection<Venda> listarPorUsuario(String emailUsuario) {
        String sql = "SELECT * FROM venda WHERE email_usuario = ?";
        Collection<Venda> vendas = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, emailUsuario);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                vendas.add(mapearVenda(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao listar vendas", e);
        }
        return vendas;
    }

    private Venda mapearVenda(ResultSet rs) throws SQLException {
        Venda venda = new Venda();
        venda.setId(rs.getLong("id"));
        venda.setEmailUsuario(rs.getString("email_usuario"));
        venda.setIngressoId(rs.getLong("ingresso_id"));
        venda.setValorPago(rs.getDouble("valor_pago"));
        return venda;
    }
}