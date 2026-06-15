package dao;

import db.DatabaseConnection;
import model.Carteira;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação SQL (MariaDB) do padrão DAO para a entidade Carteira.
 * Realiza operações CRUD na tabela "Carteira" do banco de dados.
 *
 * Contrato: implementa ICarteiraDAO (interface definida por Vinicius Romão).
 *
 * @author Raíssa Souza Santos - 284570
 * @version 1.0
 */
public class CarteiraMariaDAO implements ICarteiraDAO {

    // Referência à conexão compartilhada (Singleton)
    private final Connection conexao;

    public CarteiraMariaDAO() {
        this.conexao = DatabaseConnection.getInstancia().getConexao();
    }

    // =========================================================================
    // CREATE
    // =========================================================================

    /**
     * Insere uma nova carteira no banco de dados.
     * O id é gerado automaticamente pelo banco (AUTO_INCREMENT).
     *
     * @param carteira objeto Carteira a ser persistido
     */
    @Override
    public void inserir(Carteira carteira) {
        final String sql = "INSERT INTO Carteira (titular, corretora) VALUES (?, ?)";

        try (PreparedStatement ps = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, carteira.getTitular());
            ps.setString(2, carteira.getCorretora());
            ps.executeUpdate();

            // Atualiza o id do objeto com o valor gerado pelo banco
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    carteira.setId(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir carteira: " + e.getMessage(), e);
        }
    }

    // =========================================================================
    // READ — busca por id
    // =========================================================================

    /**
     * Busca uma carteira pelo seu identificador único.
     *
     * @param id identificador da carteira
     * @return objeto Carteira ou null se não encontrado
     */
    @Override
    public Carteira buscarPorId(int id) {
        final String sql = "SELECT id, titular, corretora FROM Carteira WHERE id = ?";

        try (PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearCarteira(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar carteira por id: " + e.getMessage(), e);
        }

        return null; // não encontrado
    }

    // =========================================================================
    // READ — listar todas (ordenadas por id)
    // =========================================================================

    /**
     * Retorna todas as carteiras ordenadas pelo identificador.
     *
     * @return lista de Carteiras ordenada por id
     */
    @Override
    public List<Carteira> listarOrdenadoPorId() {
        final String sql = "SELECT id, titular, corretora FROM Carteira ORDER BY id ASC";
        return executarListagem(sql);
    }

    // =========================================================================
    // READ — listar todas (ordenadas por nome do titular)
    // =========================================================================

    /**
     * Retorna todas as carteiras ordenadas pelo nome do titular.
     *
     * @return lista de Carteiras ordenada por titular
     */
    @Override
    public List<Carteira> listarOrdenadoPorTitular() {
        final String sql = "SELECT id, titular, corretora FROM Carteira ORDER BY titular ASC";
        return executarListagem(sql);
    }

    // =========================================================================
    // UPDATE
    // =========================================================================

    /**
     * Atualiza os dados de uma carteira existente.
     *
     * @param carteira objeto Carteira com os dados atualizados (id deve estar preenchido)
     */
    @Override
    public void atualizar(Carteira carteira) {
        final String sql = "UPDATE Carteira SET titular = ?, corretora = ? WHERE id = ?";

        try (PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, carteira.getTitular());
            ps.setString(2, carteira.getCorretora());
            ps.setInt(3, carteira.getId());

            int linhasAfetadas = ps.executeUpdate();
            if (linhasAfetadas == 0) {
                throw new RuntimeException("Nenhuma carteira encontrada com id: " + carteira.getId());
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar carteira: " + e.getMessage(), e);
        }
    }

    // =========================================================================
    // DELETE
    // =========================================================================

    /**
     * Remove uma carteira pelo id.
     * As movimentações associadas são removidas automaticamente (ON DELETE CASCADE).
     *
     * @param id identificador da carteira a ser removida
     */
    @Override
    public void excluir(int id) {
        final String sql = "DELETE FROM Carteira WHERE id = ?";

        try (PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setInt(1, id);

            int linhasAfetadas = ps.executeUpdate();
            if (linhasAfetadas == 0) {
                throw new RuntimeException("Nenhuma carteira encontrada com id: " + id);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir carteira: " + e.getMessage(), e);
        }
    }

    // =========================================================================
    // Auxiliar — mapeamento ResultSet → Carteira
    // =========================================================================

    /**
     * Executa uma query de listagem e retorna a lista de Carteiras mapeadas.
     */
    private List<Carteira> executarListagem(String sql) {
        List<Carteira> lista = new ArrayList<>();

        try (PreparedStatement ps = conexao.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearCarteira(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar carteiras: " + e.getMessage(), e);
        }

        return lista;
    }

    /**
     * Converte uma linha do ResultSet em um objeto Carteira.
     *
     * @param rs ResultSet posicionado na linha desejada
     * @return objeto Carteira preenchido
     * @throws SQLException em caso de erro na leitura do ResultSet
     */
    private Carteira mapearCarteira(ResultSet rs) throws SQLException {
        Carteira c = new Carteira();
        c.setId(rs.getInt("id"));
        c.setTitular(rs.getString("titular"));
        c.setCorretora(rs.getString("corretora"));
        return c;
    }
}