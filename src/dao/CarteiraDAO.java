package src.dao;
import java.util.List;
//testando

import src.model.Carteira;

public interface CarteiraDAO {
    void inserir(Carteira carteira);
    Carteira consultar(int identificador);
    void atualizar(Carteira carteira);
    void excluir(int identificador);
    List<Carteira> listarTodas();
}
