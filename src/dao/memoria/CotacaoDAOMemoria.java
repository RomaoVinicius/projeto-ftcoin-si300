package src.dao.memoria;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import src.dao.CotacaoDAO;
import src.model.Cotacao;

public class CotacaoDAOMemoria implements CotacaoDAO {
    private final List<Cotacao> bancoDeDados = new ArrayList<>();

    @Override
    public void inserir(Cotacao cotacao) {
        if (consultar(cotacao.getData()) == null) {
            bancoDeDados.add(cotacao);
        } else {
            System.err.println("Cotação já existe para esta data.");
        }
    }

    @Override
    public Cotacao consultar(LocalDate data) {
        for (Cotacao c : bancoDeDados) {
            if (c.getData().equals(data)) {
                return c;
            }
        }
        return null;
    }

    @Override
    public void atualizar(Cotacao cotacao) {
        for (int i = 0; i < bancoDeDados.size(); i++) {
            if (bancoDeDados.get(i).getData().equals(cotacao.getData())) {
                bancoDeDados.set(i, cotacao);
                return;
            }
        }
    }

    @Override
    public void excluir(LocalDate data) {
        bancoDeDados.removeIf(c -> c.getData().equals(data));
    }

    @Override
    public List<Cotacao> listarTodas() {
        return new ArrayList<>(bancoDeDados);
    }
}