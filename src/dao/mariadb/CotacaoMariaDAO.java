package src.dao.mariadb;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import src.dao.CotacaoDAO;
import src.db.DatabaseConnection;
import src.model.Cotacao;

public class CotacaoMariaDAO implements CotacaoDAO {

    @Override
    public void inserir(Cotacao cotacao) {
        try {
            String tableName = resolveTableName();
            String dateColumn = resolveColumnName(tableName, "Data", "data", "dataCotacao");
            String valueColumn = resolveColumnName(tableName, "Cotacao", "cotacao");
            String sql = "INSERT INTO " + tableName + " (" + dateColumn + ", " + valueColumn + ") VALUES (?, ?) " +
                    "ON DUPLICATE KEY UPDATE " + valueColumn + " = ?";
            try (PreparedStatement ps = DatabaseConnection.getInstancia().getConexao().prepareStatement(sql)) {
                ps.setDate(1, Date.valueOf(cotacao.getData()));
                ps.setBigDecimal(2, cotacao.getValor());
                ps.setBigDecimal(3, cotacao.getValor());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Erro ao inserir cotação: " + e.getMessage());
        }
    }

    @Override
    public Cotacao consultar(LocalDate data) {
        return consultarPorData(data);
    }

    public Cotacao consultarPorData(LocalDate data) {
        try {
            String tableName = resolveTableName();
            String dateColumn = resolveColumnName(tableName, "Data", "data", "dataCotacao");
            String valueColumn = resolveColumnName(tableName, "Cotacao", "cotacao");
            String sql = "SELECT * FROM " + tableName + " WHERE " + dateColumn + " = ?";
            try (PreparedStatement ps = DatabaseConnection.getInstancia().getConexao().prepareStatement(sql)) {
                ps.setDate(1, Date.valueOf(data));
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return mapear(rs, dateColumn, valueColumn);
                    }
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
        throw new UnsupportedOperationException("Exclusão de cotação por id numérico não aplicável, a tabela usa a data como chave.");
    }

    @Override
    public List<Cotacao> listarTodas() {
        List<Cotacao> lista = new ArrayList<>();
        try {
            String tableName = resolveTableName();
            String dateColumn = resolveColumnName(tableName, "Data", "data", "dataCotacao");
            String valueColumn = resolveColumnName(tableName, "Cotacao", "cotacao");
            String sql = "SELECT * FROM " + tableName + " ORDER BY " + dateColumn;
            try (Statement st = DatabaseConnection.getInstancia().getConexao().createStatement();
                 ResultSet rs = st.executeQuery(sql)) {
                while (rs.next()) {
                    lista.add(mapear(rs, dateColumn, valueColumn));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar cotações: " + e.getMessage());
        }
        return lista;
    }

    private Cotacao mapear(ResultSet rs, String dateColumn, String valueColumn) throws SQLException {
        BigDecimal valor = rs.getBigDecimal(valueColumn);
        LocalDate data = rs.getDate(dateColumn).toLocalDate();
        return new Cotacao(data, valor);
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
