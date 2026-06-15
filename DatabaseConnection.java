package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DatabaseConnection { // gerencia conexão com o banco de dados MariaDB usando o padrão Singleton


    private static final String URL      = "jdbc:mariadb://localhost:3306/ftcoin";
    private static final String USUARIO  = "root";
    private static final String SENHA    = "sua_senha_aqui";

    //Instância única (Singleton).
    private static DatabaseConnection instancia;

    // Objeto de conexão JDBC
    private Connection conexao;

    private DatabaseConnection() {// construtor privado para evitar instanciamento externo
        try {
            // Registra o driver MariaDB 
            Class.forName("org.mariadb.jdbc.Driver");
            this.conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
            System.out.println("[DB] Conexão com MariaDB estabelecida com sucesso.");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("[DB] Driver MariaDB não encontrado: " + e.getMessage(), e);
        } catch (SQLException e) {
            throw new RuntimeException("[DB] Falha ao conectar ao banco de dados: " + e.getMessage(), e);
        }
    }

    // Retorna a instância única de DatabaseConnection, cria a instância se ainda não existir ou se a conexão estiver fechada.
    // @return instância única de DatabaseConnection
    
    public static DatabaseConnection getInstancia() {// método de acesso à instância única
        try {
            if (instancia == null || instancia.conexao.isClosed()) {
                instancia = new DatabaseConnection();
            }
        } catch (SQLException e) {
            instancia = new DatabaseConnection();
        }
        return instancia;
    }

    // Retorna o objeto Connection para uso nos DAOs @return conexão ativa com o banco de dados
    public Connection getConexao() {
        return conexao;
    }

    // Encerra a conexão com o banco de dados deve ser chamado ao finalizar a aplicação.
    public void fecharConexao() {
        try {
            if (conexao != null && !conexao.isClosed()) {
                conexao.close();
                System.out.println("[DB] Conexão com MariaDB encerrada.");
            }
        } catch (SQLException e) {
            System.err.println("[DB] Erro ao fechar conexão: " + e.getMessage());
        }
    }
}