package src.dao.mariadb;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import src.dao.MovimentacaoDAO;
import src.db.DatabaseConnection;
import src.model.Movimentacao;
import src.model.TipoMovimentacao;

// implementação de persistência da entidade Movimentacao em banco MariaDB
public class MovimentacaoMariaDAO implements MovimentacaoDAO {

    @Override
    public void inserir(Movimentacao movimentacao) {
        String sql = "INSERT INTO MOVIMENTACAO (IdCarteira, Data, TipoOperacao, Quantidade) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = DatabaseConnection.getInstancia()
                .getConexao().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            movimentacao.setCotacaoNaData(buscarCotacaoPorData(movimentacao.getDataOperacao()));
            ps.setInt(1, movimentacao.getIdCarteira());
            ps.setDate(2, Date.valueOf(movimentacao.getDataOperacao()));
            ps.setString(3, String.valueOf(movimentacao.getTipoMovimentacao().getCodigo()));
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
        String sql = "SELECT * FROM MOVIMENTACAO WHERE IdMovimento = ?";
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
        String sql = "UPDATE MOVIMENTACAO SET Data = ?, TipoOperacao = ?, Quantidade = ? WHERE IdMovimento = ?";
        try (PreparedStatement ps = DatabaseConnection.getInstancia()
                .getConexao().prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(movimentacao.getDataOperacao()));
            ps.setString(2, String.valueOf(movimentacao.getTipoMovimentacao().getCodigo()));
            ps.setBigDecimal(3, movimentacao.getQuantidade());
            ps.setInt(4, movimentacao.getIdMovimento());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar movimentação: " + e.getMessage());
        }
    }

    @Override
    public void excluir(int idMovimento) {
        String sql = "DELETE FROM MOVIMENTACAO WHERE IdMovimento = ?";
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
        String sql = "SELECT * FROM MOVIMENTACAO";
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
        String sql = "SELECT * FROM MOVIMENTACAO WHERE IdCarteira = ? ORDER BY Data";
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
        BigDecimal quantidade = rs.getBigDecimal("Quantidade");
        LocalDate dataOperacao = rs.getDate("Data").toLocalDate();
        BigDecimal cotacaoNaData = buscarCotacaoPorData(dataOperacao);
        String tipoOperacao = rs.getString("TipoOperacao");
        TipoMovimentacao tipo = TipoMovimentacao.fromCodigo(tipoOperacao.charAt(0));
        return new Movimentacao(
                rs.getInt("IdMovimento"),
                rs.getInt("IdCarteira"),
                dataOperacao,
                tipo,
                quantidade,
                cotacaoNaData
        );
    }

    private BigDecimal buscarCotacaoPorData(LocalDate data) {
        if (data == null) {
            return null;
        }
        try {
            String tableName = resolveTableName();
            String dateColumn = resolveColumnName(tableName, "Data", "data", "dataCotacao");
            String valueColumn = resolveColumnName(tableName, "Cotacao", "cotacao");
            String sql = "SELECT " + valueColumn + " FROM " + tableName + " WHERE " + dateColumn + " = ?";
            try (PreparedStatement ps = DatabaseConnection.getInstancia().getConexao().prepareStatement(sql)) {
                ps.setDate(1, Date.valueOf(data));
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getBigDecimal(valueColumn);
                    }
                }
            }
        } catch (SQLException e) {
            return null;
        }
        return null;
    }

    private String resolveTableName() throws SQLException {
        DatabaseMetaData meta = DatabaseConnection.getInstancia().getConexao().getMetaData();
        try (ResultSet rs = meta.getTables(null, null, "ORACULO", null)) {
            if (rs.next()) {
                return "ORACULO";
            }
        }
        try (ResultSet rs = meta.getTables(null, null, "ORACULAO", null)) {
            if (rs.next()) {
                return "ORACULAO";
            }
        }
        try (Statement st = DatabaseConnection.getInstancia().getConexao().createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS ORACULO (Data DATE NOT NULL, Cotacao DECIMAL(6,2) NOT NULL, PRIMARY KEY (Data))");
        }
        return "ORACULO";
    }

    private String resolveColumnName(String tableName, String... candidates) throws SQLException {
        DatabaseMetaData meta = DatabaseConnection.getInstancia().getConexao().getMetaData();
        try (ResultSet rs = meta.getColumns(null, null, tableName, null)) {
            while (rs.next()) {
                String columnName = rs.getString("COLUMN_NAME");
                for (String candidate : candidates) {
                    if (candidate.equalsIgnoreCase(columnName)) {
                        return columnName;
                    }
                }
            }
        }
        return candidates[0];
    }
}
