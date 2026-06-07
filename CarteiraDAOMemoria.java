import java.util.ArrayList;
import java.util.List;

public class CarteiraDAOMemoria implements CarteiraDAO {
    private final List<Carteira> bancoDeDados = new ArrayList<>();

    @Override
    public void inserir(Carteira carteira) {
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
    public void atualizar(Carteira carteiraAtualizada) {
        for (int i = 0; i < bancoDeDados.size(); i++) {
            if (bancoDeDados.get(i).getIdentificador() == carteiraAtualizada.getIdentificador()) {
                bancoDeDados.set(i, carteiraAtualizada);
                return;
            }
        }
    }

    @Override
    public void excluir(int identificador) {
        bancoDeDados.removeIf(c -> c.getIdentificador() == identificador);
    }

    @Override
    public List<Carteira> listarTodas() {
        return new ArrayList<>(bancoDeDados);
    }
}