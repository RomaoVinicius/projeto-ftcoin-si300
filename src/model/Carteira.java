package src.dao;

import src.model.Carteira;
import src.dao.memoria.CarteiraDAOMemoria;
import src.dao.mariadb.CarteiraDAOMariadb; 
import java.util.List;

/**
 * Interface selada para o padrão DAO da entidade Carteira.
 * <p>
 * Define os contratos para as operações de CRUD (Create, Read, Update, Delete)
 * referentes às carteiras. Utiliza o recurso de Sealed Interfaces do Java para
 * garantir que apenas as implementações de Memória e MariaDB sejam permitidas.
 * </p>
 *
 * @author Ana Julia Bandeira Maximo
 * @version 2.0
 * @since 2026
 */
public sealed interface CarteiraDAO permits CarteiraDAOMemoria, CarteiraDAOMariadb {

    /**
     * Inclui uma nova carteira no banco de dados.
     *
     * @param carteira o objeto Carteira a ser inserido
     * @throws Exception caso ocorra um erro de validação (ex: ID duplicado)
     */
    void incluir(Carteira carteira) throws Exception;

    /**
     * Consulta uma carteira pelo seu identificador único.
     *
     * @param identificador o ID da carteira
     * @return a Carteira encontrada, ou {@code null} se não existir
     */
    Carteira consultar(int identificador);

    /**
     * Edita os dados de uma carteira já existente.
     *
     * @param carteira o objeto Carteira com os dados atualizados
     * @throws Exception caso a carteira não seja encontrada
     */
    void editar(Carteira carteira) throws Exception;

    /**
     * Exclui uma carteira com base no seu identificador.
     *
     * @param identificador o ID da carteira a ser removida
     * @throws Exception caso a carteira não seja encontrada
     */
    void excluir(int identificador) throws Exception;

    /**
     * Retorna a lista de todas as carteiras cadastradas.
     *
     * @return uma lista contendo todas as carteiras
     */
    List<Carteira> listarTodas();
}
