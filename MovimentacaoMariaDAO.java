package dao;

import db.DatabaseConnection;
import model.Movimentacao;
import model.TipoMovimentacao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação SQL (MariaDB) do padrão DAO para a entidade Movimentacao.
 * Realiza operações na tabela "Movimentacao" do banco de dados.
 *
 * Contrato: implementa IMovimentacaoDAO (interface definida por Vinicius Romão).
 *
 * @author Raíssa Souza Santos - 284570
 * @version 1.0
 */
public class MovimentacaoMariaDAO implements IMovimentacaoDAO {

    private final Connection conexao;

    public MovimentacaoMariaDAO() {
        this.conexao = DatabaseConnection.getInstancia().getConexao();
    }

    // =========================================================================
    // CREATE
    // =========================================================================

    /**
     * Insere uma nova movimentação (compra ou venda) no banco de dados.
     *
     * @param movimentacao objeto Movimentacao a ser persistido
     */
    @Override
    public void inserir(Movimentacao movimentacao) {
        final String sql =
            "INSERT INTO Movimentacao (id_carteira, data_operacao, tipo_operacao, quantidade) " +
            "VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, movimentacao.getIdCarteira());
            ps.setDate(2, Date.valueOf(movimentacao.getDataOperacao()));       // java.time.LocalDate → java.sql.Date
            ps.setString(3, movimentacao.getTipo().getCodigo());               // 'C' ou 'V'
            ps.setBigDecimal(4, movimentacao.getQuantidade());
            ps.executeUpdate();

            // Atualiza o id gerado pelo banco no objeto
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    movimentacao.setIdMovimentacao(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir movimentação: " + e.getMessage(), e);
        }
    }

    // =========================================================================
    // READ — busca por id de movimentação
    // =========================================================================

    /**
     * Busca uma movimentação pelo seu identificador único.
     *
     * @param idMovimentacao identificador da movimentação
     * @return objeto Movimentacao ou null se não encontrado
     */
    @Override
    public Movimentacao buscarPorId(int idMovimentacao) {
        final String sql =
            "SELECT id_movimentacao, id_carteira, data_operacao, tipo_operacao, quantidade " +
            "FROM Movimentacao WHERE id_movimentacao = ?";

        try (PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setInt(1, idMovimentacao);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearMovimentacao(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar movimentação por id: " + e.getMessage(), e);
        }

        return null;
    }

    // =========================================================================
    // READ — listar todas as movimentações de uma carteira
    // =========================================================================

    /**
     * Retorna o histórico completo de movimentações de uma carteira, em ordem cronológica.
     *
     * @param idCarteira identificador da carteira
     * @return lista de Movimentacoes ordenada por data
     */
    @Override
    public List<Movimentacao> listarPorCarteira(int idCarteira) {
        final String sql =
            "SELECT id_movimentacao, id_carteira, data_operacao, tipo_operacao, quantidade " +
            "FROM Movimentacao WHERE id_carteira = ? ORDER BY data_operacao ASC, id_movimentacao ASC";

        List<Movimentacao> lista = new ArrayList<>();

        try (PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setInt(1, idCarteira);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearMovimentacao(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar movimentações da carteira: " + e.getMessage(), e);
        }

        return lista;
    }

    // =========================================================================
    // DELETE
    // =========================================================================

    /**
     * Remove uma movimentação pelo seu identificador.
     *
     * @param idMovimentacao identificador da movimentação a ser removida
     */
    @Override
    public void excluir(int idMovimentacao) {
        final String sql = "DELETE FROM Movimentacao WHERE id_movimentacao = ?";

        try (PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setInt(1, idMovimentacao);

            int linhasAfetadas = ps.executeUpdate();
            if (linhasAfetadas == 0) {
                throw new RuntimeException("Nenhuma movimentação encontrada com id: " + idMovimentacao);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir movimentação: " + e.getMessage(), e);
        }
    }

    // =========================================================================
    // SALDO — quantidade atual de moedas em uma carteira
    // =========================================================================

    /**
     * Calcula o saldo atual de moedas da carteira somando compras e subtraindo vendas.
     *
     * @param idCarteira identificador da carteira
     * @return saldo em moeda virtual (BigDecimal)
     */
    @Override
    public java.math.BigDecimal calcularSaldo(int idCarteira) {
        final String sql =
            "SELECT " +
            "  COALESCE(SUM(CASE WHEN tipo_operacao = 'C' THEN quantidade ELSE 0 END), 0) " +
            "  - COALESCE(SUM(CASE WHEN tipo_operacao = 'V' THEN quantidade ELSE 0 END), 0) " +
            "  AS saldo " +
            "FROM Movimentacao WHERE id_carteira = ?";

        try (PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setInt(1, idCarteira);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("saldo");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao calcular saldo: " + e.getMessage(), e);
        }

        return java.math.BigDecimal.ZERO;
    }

    // =========================================================================
    // Auxiliar — mapeamento ResultSet → Movimentacao
    // =========================================================================

    /**
     * Converte uma linha do ResultSet em um objeto Movimentacao.
     *
     * @param rs ResultSet posicionado na linha desejada
     * @return objeto Movimentacao preenchido
     * @throws SQLException em caso de erro na leitura
     */
    private Movimentacao mapearMovimentacao(ResultSet rs) throws SQLException {
        Movimentacao m = new Movimentacao();
        m.setIdMovimentacao(rs.getInt("id_movimentacao"));
        m.setIdCarteira(rs.getInt("id_carteira"));
        m.setDataOperacao(rs.getDate("data_operacao").toLocalDate());  // java.sql.Date → LocalDate
        m.setTipo(TipoMovimentacao.fromCodigo(rs.getString("tipo_operacao")));
        m.setQuantidade(rs.getBigDecimal("quantidade"));
        return m;
    }
}