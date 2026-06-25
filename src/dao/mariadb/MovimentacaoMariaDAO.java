package src.dao.mariadb;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import src.dao.MovimentacaoDAO;
import src.model.Movimentacao;

import java.math.BigDecimal;// adicionado para uso de BigDecimal, sugestão do VScode 

// implementação de persistência da entidade Movimentacao em banco MariaDB
public class MovimentacaoMariaDAO implements MovimentacaoDAO {

    @Override
    public void inserir(Movimentacao movimentacao) {
        String sql = "INSERT INTO Movimentacao (idCarteira, dataOperacao, tipoOperacao, quantidade) " + "VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = DatabaseConnection.getInstancia()
                .getConexao().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, movimentacao.getIdCarteira());
            ps.setDate(2, Date.valueOf(movimentacao.getDataOperacao()));
            ps.setString(3, String.valueOf(movimentacao.getTipoOperacao()));
            ps.setBigDecimal(4, movimentacao.getQuantidade());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    movimentacao.setIdMovimento(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao inserir movimentação: " + e.getMessage());
        }
    }

    @Override
    public Movimentacao consultar(int idMovimento) {
        String sql = "SELECT * FROM Movimentacao WHERE idMovimento = ?";
        try (PreparedStatement ps = DatabaseConnection.getInstancia()
                .getConexao().prepareStatement(sql)) {

            ps.setInt(1, idMovimento);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao consultar movimentação: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void atualizar(Movimentacao movimentacao) {
        String sql = "UPDATE Movimentacao SET dataOperacao = ?, tipoOperacao = ?, quantidade = ? " + "WHERE idMovimento = ?";
        try (PreparedStatement ps = DatabaseConnection.getInstancia()
                .getConexao().prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(movimentacao.getDataOperacao()));
            ps.setString(2, String.valueOf(movimentacao.getTipoOperacao()));
            ps.setBigDecimal(3, movimentacao.getQuantidade());
            ps.setInt(4, movimentacao.getIdMovimento());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar movimentação: " + e.getMessage());
        }
    }

    @Override
    public void excluir(int idMovimento) {
        String sql = "DELETE FROM Movimentacao WHERE idMovimento = ?";
        try (PreparedStatement ps = DatabaseConnection.getInstancia()
                .getConexao().prepareStatement(sql)) {

            ps.setInt(1, idMovimento);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao excluir movimentação: " + e.getMessage());
        }
    }

    @Override
    public List<Movimentacao> listarTodas() {
        List<Movimentacao> lista = new ArrayList<>();
        String sql = "SELECT * FROM Movimentacao";
        try (Statement st = DatabaseConnection.getInstancia()
                .getConexao().createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar movimentações: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Movimentacao> listarPorCarteira(int idCarteira) {
        List<Movimentacao> lista = new ArrayList<>();
        String sql = "SELECT * FROM Movimentacao WHERE idCarteira = ? ORDER BY dataOperacao";
        try (PreparedStatement ps = DatabaseConnection.getInstancia()
                .getConexao().prepareStatement(sql)) {

            ps.setInt(1, idCarteira);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar movimentações da carteira: " + e.getMessage());
        }
        return lista;
    }

    private Movimentacao mapear(ResultSet rs) throws SQLException {
        BigDecimal quantidade = rs.getBigDecimal("quantidade");
        return new Movimentacao(
                rs.getInt("idMovimento"),
                rs.getInt("idCarteira"),
                rs.getDate("dataOperacao").toLocalDate(),
                rs.getString("tipoOperacao").charAt(0),
                quantidade
        );
    }
}