package src.model;

/**
 * Representa uma carteira de moedas virtuais no sistema FT_Coin.
 * <p>
 * Esta classe é o modelo que armazena os dados básicos da carteira,
 * refletindo a tabela Carteira do banco de dados.
 * </p>
 *
 * @author Ana Julia Bandeira Maximo
 * @version 2.0
 * @since 2026
 */
public class Carteira {
    
    /**
     * O identificador único da carteira.
     */
    private int identificador;
    
    /**
     * O nome completo do titular da carteira.
     */
    private String nomeTitular;
    
    /**
     * O nome da corretora responsável.
     */
    private String corretora;

    /**
     * Constrói uma nova {@code Carteira} com os dados especificados.
     *
     * @param identificador o identificador único da carteira
     * @param nomeTitular o nome do titular da carteira
     * @param corretora o nome da corretora
     */
    public Carteira(int identificador, String nomeTitular, String corretora) {
        this.identificador = identificador;
        this.nomeTitular = nomeTitular;
        this.corretora = corretora;
    }

    /**
     * Retorna o identificador único da carteira.
     *
     * @return o identificador da carteira
     */
    public int getIdentificador() {
        return identificador;
    }

    /**
     * Define o identificador único da carteira.
     *
     * @param identificador o novo identificador a ser atribuído
     */
    public void setIdentificador(int identificador) {
        this.identificador = identificador;
    }

    /**
     * Retorna o nome do titular da carteira.
     *
     * @return o nome do titular
     */
    public String getNomeTitular() {
        return nomeTitular;
    }

    /**
     * Define o nome do titular da carteira.
     *
     * @param nomeTitular o novo nome do titular a ser atribuído
     */
    public void setNomeTitular(String nomeTitular) {
        this.nomeTitular = nomeTitular;
    }

    /**
     * Retorna a corretora da carteira.
     *
     * @return a corretora
     */
    public String getCorretora() {
        return corretora; 
    }

    /**
     * Define a corretora da carteira.
     *
     * @param corretora a nova corretora a ser atribuída
     */
    public void setCorretora(String corretora) {
        this.corretora = corretora; 
    }

    @Override
    public String toString() {
        return String.format("ID: %d | Titular: %s | Corretora: %s", identificador, nomeTitular, corretora);
    }
}
