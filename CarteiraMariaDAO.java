import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// implementação de persistência da entidade Carteira em banco MariaD
public class CarteiraMariaDAO implements CarteiraDAO {

    @Override
    public void inserir(Carteira carteira) {
        String sql = "INSERT INTO Carteira (identificador, nomeTitular, corretora) VALUES (?, ?, ?)";
        try (PreparedStatement ps = DatabaseConnection.getInstancia()
                .getConexao().prepareStatement(sql)) {

            ps.setInt(1, carteira.getIdentificador());
            ps.setString(2, carteira.getNomeTitular());
            ps.setString(3, carteira.getCorretora());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao inserir carteira: " + e.getMessage());
        }
    }

    @Override
    public Carteira consultar(int identificador) {
        String sql = "SELECT * FROM Carteira WHERE identificador = ?";
        try (PreparedStatement ps = DatabaseConnection.getInstancia()
                .getConexao().prepareStatement(sql)) {

            ps.setInt(1, identificador);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao consultar carteira: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void atualizar(Carteira carteira) {
        String sql = "UPDATE Carteira SET nomeTitular = ?, corretora = ? WHERE identificador = ?";
        try (PreparedStatement ps = DatabaseConnection.getInstancia()
                .getConexao().prepareStatement(sql)) {

            ps.setString(1, carteira.getNomeTitular());
            ps.setString(2, carteira.getCorretora());
            ps.setInt(3, carteira.getIdentificador());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar carteira: " + e.getMessage());
        }
    }

    @Override
    public void excluir(int identificador) {
        String sql = "DELETE FROM Carteira WHERE identificador = ?";
        try (PreparedStatement ps = DatabaseConnection.getInstancia()
                .getConexao().prepareStatement(sql)) {

            ps.setInt(1, identificador);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao excluir carteira: " + e.getMessage());
        }
    }

    @Override
    public List<Carteira> listarTodas() {
        List<Carteira> lista = new ArrayList<>();
        String sql = "SELECT * FROM Carteira";
        try (Statement st = DatabaseConnection.getInstancia()
                .getConexao().createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar carteiras: " + e.getMessage());
        }
        return lista;
    }

    private Carteira mapear(ResultSet rs) throws SQLException {
        return new Carteira(
                rs.getInt("identificador"),
                rs.getString("nomeTitular"),
                rs.getString("corretora")
        );
    }
}