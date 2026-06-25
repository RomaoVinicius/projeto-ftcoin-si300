package src.dao;
import java.time.LocalDate;
import java.util.List;

import src.model.Cotacao;

public interface CotacaoDAO {
    void inserir(Cotacao cotacao);
    Cotacao consultar(LocalDate data); 
    void atualizar(Cotacao cotacao);
    void excluir(LocalDate data);      
    List<Cotacao> listarTodas();
}