package dao;

import db.DatabaseConnection;
import model.Cotacao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação SQL (MariaDB) do padrão DAO para a entidade Cotacao (Oráculo).
 * Realiza operações na tabela "Cotacao" do banco de dados.
 *
 * Contrato: implementa ICotacaoDAO (interface definida por Vinicius Romão).
 *
 * @author Raíssa Souza Santos - 284570
 * @version 1.0
 */
public class CotacaoMariaDAO implements ICotacaoDAO {

    private final Connection conexao;

    public CotacaoMariaDAO() {
        this.conexao = DatabaseConnection.getInstancia().getConexao();
    }

    // =========================================================================
    // CREATE / UPDATE (UPSERT)
    // =========================================================================

    /**
     * Insere ou atualiza a cotação de uma data específica.
     * Usa INSERT ... ON DUPLICATE KEY UPDATE para evitar erros de chave duplicada.
     *
     * @param cotacao objeto Cotacao a ser persistido
     */
    @Override
    public void salvar(Cotacao cotacao) {
        final String sql =
            "INSERT INTO Cotacao (data_cotacao, cotacao) VALUES (?, ?) " +
            "ON DUPLICATE KEY UPDATE cotacao = VALUES(cotacao)";

        try (PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(cotacao.getData()));
            ps.setBigDecimal(2, cotacao.getCotacao());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar cotação: " + e.getMessage(), e);
        }
    }

    // =========================================================================
    // READ — busca por data exata
    // =========================================================================

    /**
     * Busca a cotação de uma data específica.
     *
     * @param data data da cotação
     * @return objeto Cotacao ou null se não houver cotação para a data
     */
    @Override
    public Cotacao buscarPorData(LocalDate data) {
        final String sql = "SELECT data_cotacao, cotacao FROM Cotacao WHERE data_cotacao = ?";

        try (PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(data));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearCotacao(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar cotação por data: " + e.getMessage(), e);
        }

        return null;
    }

    // =========================================================================
    // READ — cotação mais recente disponível
    // =========================================================================

    /**
     * Retorna a cotação mais recente registrada no oráculo.
     * Útil quando a data exata não está disponível.
     *
     * @return objeto Cotacao mais recente ou null se o oráculo estiver vazio
     */
    @Override
    public Cotacao buscarMaisRecente() {
        final String sql =
            "SELECT data_cotacao, cotacao FROM Cotacao ORDER BY data_cotacao DESC LIMIT 1";

        try (PreparedStatement ps = conexao.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return mapearCotacao(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar cotação mais recente: " + e.getMessage(), e);
        }

        return null;
    }

    // =========================================================================
    // READ — listar todas as cotações em ordem cronológica
    // =========================================================================

    /**
     * Retorna todas as cotações registradas, ordenadas da mais antiga para a mais recente.
     *
     * @return lista de Cotacoes em ordem cronológica
     */
    @Override
    public List<Cotacao> listarTodas() {
        final String sql = "SELECT data_cotacao, cotacao FROM Cotacao ORDER BY data_cotacao ASC";
        List<Cotacao> lista = new ArrayList<>();

        try (PreparedStatement ps = conexao.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearCotacao(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar cotações: " + e.getMessage(), e);
        }

        return lista;
    }

    // =========================================================================
    // DELETE
    // =========================================================================

    /**
     * Remove a cotação de uma data específica.
     *
     * @param data data da cotação a ser removida
     */
    @Override
    public void excluir(LocalDate data) {
        final String sql = "DELETE FROM Cotacao WHERE data_cotacao = ?";

        try (PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(data));

            int linhasAfetadas = ps.executeUpdate();
            if (linhasAfetadas == 0) {
                throw new RuntimeException("Nenhuma cotação encontrada para a data: " + data);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir cotação: " + e.getMessage(), e);
        }
    }

    // =========================================================================
    // Auxiliar — mapeamento ResultSet → Cotacao
    // =========================================================================

    /**
     * Converte uma linha do ResultSet em um objeto Cotacao.
     *
     * @param rs ResultSet posicionado na linha desejada
     * @return objeto Cotacao preenchido
     * @throws SQLException em caso de erro na leitura
     */
    private Cotacao mapearCotacao(ResultSet rs) throws SQLException {
        Cotacao c = new Cotacao();
        c.setData(rs.getDate("data_cotacao").toLocalDate());
        c.setCotacao(rs.getBigDecimal("cotacao"));
        return c;
    }
}