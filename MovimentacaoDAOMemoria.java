import java.util.ArrayList;
import java.util.List;

public class MovimentacaoDAOMemoria implements MovimentacaoDAO {
    private final List<Movimentacao> bancoDeDados = new ArrayList<>();

    @Override
    public void inserir(Movimentacao movimentacao) {
        bancoDeDados.add(movimentacao);
    }

    @Override
    public Movimentacao consultar(int id) {
        for (Movimentacao m : bancoDeDados) {
            if (m.getIdentificador() == id) {
                return m;
            }
        }
        return null;
    }

    @Override
    public void atualizar(Movimentacao movimentacao) {
        for (int i = 0; i < bancoDeDados.size(); i++) {
            if (bancoDeDados.get(i).getIdentificador() == movimentacao.getIdentificador()) {
                bancoDeDados.set(i, movimentacao);
                return;
            }
        }
    }

    @Override
    public void excluir(int id) {
        bancoDeDados.removeIf(m -> m.getIdentificador() == id);
    }

    @Override
    public List<Movimentacao> listarTodas() {
        return new ArrayList<>(bancoDeDados);
    }
}