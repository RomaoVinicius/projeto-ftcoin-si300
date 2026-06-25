package src.dao.memoria;
import java.util.ArrayList;
import java.util.List;

import src.dao.MovimentacaoDAO;
import src.model.Movimentacao;

public class MovimentacaoDAOMemoria implements MovimentacaoDAO {
    private final List<Movimentacao> bancoDeDados = new ArrayList<>();

    @Override
    public void inserir(Movimentacao movimentacao) {
        bancoDeDados.add(movimentacao);
    }

    @Override
    public Movimentacao consultar(int id) {
        for (Movimentacao m : bancoDeDados) {
            if (m.getIdMovimento() == id) {
                return m;
            }
        }
        return null;
    }

    @Override
    public void atualizar(Movimentacao movimentacao) {
        for (int i = 0; i < bancoDeDados.size(); i++) {
            if (bancoDeDados.get(i).getIdMovimento() == movimentacao.getIdMovimento()) {
                bancoDeDados.set(i, movimentacao);
                return;
            }
        }
    }

    @Override
    public void excluir(int id) {
        bancoDeDados.removeIf(m -> m.getIdMovimento() == id);
    }

    @Override
    public List<Movimentacao> listarTodas() {
        return new ArrayList<>(bancoDeDados);
    }
}