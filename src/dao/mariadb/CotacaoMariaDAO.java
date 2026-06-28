package src.dao.mariadb;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import src.dao.CotacaoDAO;
import src.model.Cotacao;
import src.db.DatabaseConnection;
import src.dao.mariadb.CotacaoMariaDAO;


import java.math.BigDecimal;// adicionado para uso de BigDecimal, sugestão do VScode 
import java.time.LocalDate;// adicionada para corrigir os problemas 2 e 3  

public class CotacaoMariaDAO implements CotacaoDAO {
    // Tabela: ORACULO (maiúsculas)
    // Coluna da data: "data" (não "dataCotacao")
    // Coluna do valor: "cotacao" (não "cotacao")

    @Override
    public void inserir(Cotacao cotacao) {
        String sql = "INSERT INTO ORACULO (data, cotacao) VALUES (?, ?) " +
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

    
    public Cotacao consultar(LocalDate idDate) {
        // Como a tabela usa a data como chave, esta busca por "id" não é aplicável direntamente
        // manti para atender a interface; usar consultarPorData()
        throw new UnsupportedOperationException(
                "Use consultarPorData(LocalDate) para a implementação SQL.");
    }

    public Cotacao consultarPorData(java.time.LocalDate data) {
        String sql = "SELECT * FROM ORACULO WHERE data = ?";
        try (PreparedStatement ps = DatabaseConnection.getInstancia()
                .getConexao().prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(data));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao consultar cotação por data: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void atualizar(Cotacao cotacao) {
        inserir(cotacao); 
    }

    
    public void excluir(LocalDate idDate) {
        throw new UnsupportedOperationException(
                "Exclusão de cotação por id numerico não aplicavél, a tabela usa Data como cahve.");// mudei o texto
    }

    @Override
    public List<Cotacao> listarTodas() {
        List<Cotacao> lista = new ArrayList<>();
        String sql = "SELECT * FROM ORACULO ORDER BY data";
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
    //correção do problema 2: lê "data" e "cotacao" nomes exatos do banco.
    private Cotacao mapear(ResultSet rs) throws SQLException {
        BigDecimal valor = rs.getBigDecimal("cotacao");
        java.time.LocalDate data = rs.getDate("data").toLocalDate();
        return new Cotacao(0, data, valor);
    }
}
