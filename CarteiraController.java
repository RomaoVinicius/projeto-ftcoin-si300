//testando
public class CarteiraController {
    private final CarteiraDAO dao;

    public CarteiraController(CarteiraDAO dao) {
        this.dao = dao;
    }

    public void incluirCarteira(int id, String nome, String corretora) {
        if (dao.consultar(id) != null) {
            throw new IllegalArgumentException("Já existe uma carteira com o ID " + id);
        }
        if (nome == null || nome.trim().isEmpty() || corretora == null || corretora.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do titular e a corretora não podem ficar em branco.");
        }
        
        Carteira novaCarteira = new Carteira(id, nome, corretora);
        dao.inserir(novaCarteira);
    }

    public Carteira consultarCarteira(int id) {
        Carteira carteira = dao.consultar(id);
        if (carteira == null) {
            throw new IllegalArgumentException("Carteira não encontrada.");
        }
        return carteira;
    }

    public void editarCarteira(int id, String novoNome, String novaCorretora) {
        Carteira carteira = consultarCarteira(id); 
        
        if (novoNome == null || novoNome.trim().isEmpty() || novaCorretora == null || novaCorretora.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do titular e a corretora não podem ficar em branco.");
        }

        carteira.setNomeTitular(novoNome);
        carteira.setCorretora(novaCorretora);
        dao.atualizar(carteira);
    }

    public void excluirCarteira(int id) {
        consultarCarteira(id); 
        dao.excluir(id);
    }
}