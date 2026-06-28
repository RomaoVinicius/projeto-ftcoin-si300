package src.dao;
import java.util.List;

import src.model.Movimentacao;

public interface MovimentacaoDAO {
    void inserir(Movimentacao movimentacao);
    Movimentacao consultar(int id);
    void atualizar(Movimentacao movimentacao);
    void excluir(int id);
    List<Movimentacao> listarTodas();
}