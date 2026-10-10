package src.dao.memoria;

import src.dao.CarteiraDAO;
import src.model.Carteira;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação em memória do padrão DAO para a entidade Carteira.
 * <p>
 * Simula um banco de dados local utilizando uma lista na memória RAM.
 * Esta classe é 'final' para respeitar o contrato da interface selada CarteiraDAO.
 * </p>
 * 
 * @author Ana Julia Bandeira Maximo
 * @version 2.0
 * @since 2026
 */
public final class CarteiraDAOMemoria implements CarteiraDAO {
    
    /**
     * Lista que atua como banco de dados temporário.
     */
    private final List<Carteira> bancoDeDados = new ArrayList<>();

    @Override
    public void incluir(Carteira carteira) throws Exception {
        // Validação de negócio exigida: não pode haver IDs duplicados
        if (consultar(carteira.getIdentificador()) != null) {
            throw new Exception("O identificador " + carteira.getIdentificador() + " já existe no banco de dados.");
        }
        bancoDeDados.add(carteira);
    }

    @Override
    public Carteira consultar(int identificador) {
        for (Carteira c : bancoDeDados) {
            if (c.getIdentificador() == identificador) {
                return c;
            }
        }
        return null; // Retorna null se não encontrar
    }

    @Override
    public void editar(Carteira carteiraAtualizada) throws Exception {
        for (int i = 0; i < bancoDeDados.size(); i++) {
            if (bancoDeDados.get(i).getIdentificador() == carteiraAtualizada.getIdentificador()) {
                bancoDeDados.set(i, carteiraAtualizada);
                return;
            }
        }
        // Se o loop terminar e não encontrar a carteira, lança uma exceção
        throw new Exception("Carteira com ID " + carteiraAtualizada.getIdentificador() + " não encontrada para edição.");
    }

    @Override
    public void excluir(int identificador) throws Exception {
        boolean removido = bancoDeDados.removeIf(c -> c.getIdentificador() == identificador);
        if (!removido) {
            throw new Exception("Carteira com ID " + identificador + " não encontrada para exclusão.");
        }
    }

    @Override
    public List<Carteira> listarTodas() {
        return new ArrayList<>(bancoDeDados);
    }
}
