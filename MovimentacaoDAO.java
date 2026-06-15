import java.util.List;

public interface MovimentacaoDAO {
    void inserir(Movimentacao movimentacao);
    Movimentacao consultar(int id);
    void atualizar(Movimentacao movimentacao);
    void excluir(int id);
    List<Movimentacao> listarTodas();
}