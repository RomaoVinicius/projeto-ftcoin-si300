
package src.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import src.model.Carteira;
import src.dao.memoria.CarteiraDAOMemoria;

/**
 * Testes unitários para a classe CarteiraDAOMemoria.
 *
 * @author Ana Julia Bandeira Maximo
 * @version 2.0
 */
public class CarteiraDAOMemoriaTest {

    private CarteiraDAOMemoria dao;

    @BeforeEach
    public void setUp() {
        // Antes de cada teste, criamos um "banco de dados" novo e limpo
        dao = new CarteiraDAOMemoria();
    }

    @Test
    public void testIncluirCarteiraComSucesso() {
        Carteira carteira = new Carteira(1, "Ana Julia", "Binance");
        assertDoesNotThrow(() -> dao.incluir(carteira),
                "Não deveria lançar exceção ao incluir uma carteira nova.");
        assertEquals(1, dao.listarTodas().size(),
                "O banco de dados deve ter 1 carteira.");
    }

    @Test
    public void testIncluirCarteiraDuplicadaLancaExcecao() {
        Carteira carteira1 = new Carteira(1, "Ana Julia", "Binance");
        Carteira carteira2 = new Carteira(1, "Rafael", "Coinbase"); // Mesmo ID!

        assertDoesNotThrow(() -> dao.incluir(carteira1));

        // Testa se o sistema bloqueia o ID repetido, como exige o projeto
        Exception excecao = assertThrows(Exception.class,
                () -> dao.incluir(carteira2));
        assertTrue(excecao.getMessage().contains("já existe"));
    }

    @Test
    public void testConsultarCarteiraExistente() throws Exception {
        Carteira carteira = new Carteira(10, "João da Silva", "Foxbit");
        dao.incluir(carteira);

        Carteira encontrada = dao.consultar(10);
        assertNotNull(encontrada);
        assertEquals("João da Silva", encontrada.getNomeTitular());
    }

    @Test
    public void testExcluirCarteiraComSucesso() throws Exception {
        Carteira carteira = new Carteira(2, "Maria", "Mercado Bitcoin");
        dao.incluir(carteira);

        assertDoesNotThrow(() -> dao.excluir(2));
        assertNull(dao.consultar(2), "A carteira deve ter sido removida.");
    }

    // =====================================================
    // TESTES ACRESCENTADOS
    // =====================================================

    @Test
    public void testConsultarCarteiraInexistente() throws Exception {
        Carteira encontrada = dao.consultar(99);

        assertNull(encontrada,
                "Deve retornar null quando a carteira não existir.");
    }

    @Test
    public void testEditarCarteiraComSucesso() throws Exception {
        Carteira carteira = new Carteira(3, "Ana Julia", "Binance");
        dao.incluir(carteira);

        Carteira atualizada =
                new Carteira(3, "Ana Julia", "Coinbase");

        dao.editar(atualizada);

        Carteira encontrada = dao.consultar(3);

        assertNotNull(encontrada);
        assertEquals("Coinbase", encontrada.getCorretora(),
                "A corretora deve ser atualizada.");
    }

    @Test
    public void testEditarCarteiraInexistente() throws Exception {
        Carteira carteira = new Carteira(99, "Ana Julia", "Binance");

        assertDoesNotThrow(() -> dao.editar(carteira));
        assertNull(dao.consultar(99),
                "Uma carteira inexistente não deve ser cadastrada pela edição.");
    }

    @Test
    public void testExcluirCarteiraInexistente() throws Exception {
        assertDoesNotThrow(() -> dao.excluir(99));
        assertNull(dao.consultar(99),
                "Não deve existir carteira com esse identificador.");
    }

    @Test
    public void testListarTodasAsCarteiras() throws Exception {
        Carteira carteira1 = new Carteira(4, "Ana Julia", "Binance");
        Carteira carteira2 = new Carteira(5, "Rafael", "Coinbase");

        dao.incluir(carteira1);
        dao.incluir(carteira2);

        assertEquals(2, dao.listarTodas().size(),
                "Devem existir duas carteiras cadastradas.");
    }

    @Test
    public void testListarCarteirasSemCadastros() throws Exception {
        assertTrue(dao.listarTodas().isEmpty(),
                "A lista deve estar vazia quando não houver carteiras.");
    }
}
