package src.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Gerencia a conexão única (Singleton) com o banco de dados MariaDB.
public final class DatabaseConnection {

    private static final String URL = "jdbc:mariadb://localhost:projeto-ftcoin-si300";
    private static final String USUARIO = "root";
    private static final String SENHA = "senha";

    private static DatabaseConnection instancia;
    private Connection conexao;

    private DatabaseConnection() throws SQLException {
        conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
    }

    public static synchronized DatabaseConnection getInstancia() throws SQLException {
        if (instancia == null || instancia.conexao.isClosed()) {
            instancia = new DatabaseConnection();
        }
        return instancia;
    }

    public Connection getConexao() {
        return conexao;
    }

    public void fechar() {
        try {
            if (conexao != null && !conexao.isClosed()) {
                conexao.close();
            }
        } catch (SQLException e) {
            System.err.println("Erro ao fechar conexão: " + e.getMessage());
        }
    }
}