package src.dao.mariadb;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import src.dao.CotacaoDAO;
import src.model.Cotacao;

import java.math.BigDecimal;// adicionado para uso de BigDecimal, sugestão do VScode 

public class CotacaoMariaDAO implements CotacaoDAO {

    @Override
    public void inserir(Cotacao cotacao) {
        String sql = "INSERT INTO Oraculo (dataCotacao, cotacao) VALUES (?, ?) " +
                "ON DUPLICATE KEY UPDATE cotacao = ?";
        try (PreparedStatement ps = DatabaseConnection.getInstancia()
                .getConexao().prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(cotacao.getData()));
            ps.setBigDecimal(2, cotacao.getValor());
            ps.setBigDecimal(3, cotacao.getValor());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao inserir cotação: " + e.getMessage());
        }
    }

    @Override
    public Cotacao consultar(int id) {
        // Como a tabela usa a data como chave, esta busca por "id" não é aplicável direntamente
        // manti para atender a interface; usar consultarPorData()
        throw new UnsupportedOperationException(
                "Use consultarPorData(LocalDate) para a implementação SQL.");
    }

    public Cotacao consultarPorData(java.time.LocalDate data) {
        String sql = "SELECT * FROM Oraculo WHERE dataCotacao = ?";
        try (PreparedStatement ps = DatabaseConnection.getInstancia()
                .getConexao().prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(data));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao consultar cotação: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void atualizar(Cotacao cotacao) {
        inserir(cotacao); 
    }

    @Override
    public void excluir(int id) {
        throw new UnsupportedOperationException(
                "Exclusão por data não implementada nesta versão.");
    }

    @Override
    public List<Cotacao> listarTodas() {
        List<Cotacao> lista = new ArrayList<>();
        String sql = "SELECT * FROM Oraculo ORDER BY dataCotacao";
        try (Statement st = DatabaseConnection.getInstancia()
                .getConexao().createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar cotações: " + e.getMessage());
        }
        return lista;
    }

    private Cotacao mapear(ResultSet rs) throws SQLException {
        BigDecimal valor = rs.getBigDecimal("cotacao");
        java.time.LocalDate data = rs.getDate("dataCotacao").toLocalDate();
        return new Cotacao(0, data, valor);
    }
}