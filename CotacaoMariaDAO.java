import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import java.math.BigDecimal;// adicionado para uso de BigDecimal, sugestão do VScode 
import java.time.LocalDate;// adicionada para corrigir os problemas 2 e 3  

public class CotacaoMariaDAO implements CotacaoDAO {
    // Tabela: ORACULO (maiúsculas)
    // Coluna da data: "Data" (não "dataCotacao")
    // Coluna do valor: "Cotacao" (não "cotacao")

    @Override
    public void inserir(Cotacao cotacao) {
        String sql = "INSERT INTO ORACULO (Data, Cotacao) VALUES (?, ?) " +
                "ON DUPLICATE KEY UPDATE Cotacao = ?";
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
        String sql = "SELECT * FROM ORACULO WHERE Data = ?";
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

    @Override
    public void excluir(int id) {
        throw new UnsupportedOperationException(
                "Exclusão de cotação por id numerico não aplicavél, a tabela usa Data como cahve.");// mudei o texto
    }

    @Override
    public List<Cotacao> listarTodas() {
        List<Cotacao> lista = new ArrayList<>();
        String sql = "SELECT * FROM ORACULO ORDER BY Data";
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
    //correção do problema 2: lê "Data" e "Cotacao" — nomes exatos do banco.
    private Cotacao mapear(ResultSet rs) throws SQLException {
        BigDecimal valor = rs.getBigDecimal("Cotacao");
        java.time.LocalDate data = rs.getDate("Data").toLocalDate();
        return new Cotacao(0, data, valor);
    }
}
