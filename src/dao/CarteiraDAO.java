package src.dao;
import java.util.List;

import src.model.Carteira;

public interface CarteiraDAO {
    void inserir(Carteira carteira);
    Carteira consultar(int identificador);
    void atualizar(Carteira carteira);
    void excluir(int identificador);
    List<Carteira> listarTodas();
}
