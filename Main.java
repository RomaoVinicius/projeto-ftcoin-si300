/*
===== Main original =====
public class Main {

    public static void main(String[] args) {
    // 1. Instanciar os bancos de dados que existem
    CarteiraDAO dao = new CarteiraDAOMemoria();
    MovimentacaoDAO movDao = new MovimentacaoDAOMemoria(); 
    
    // 2. Controladores e Views de Carteira
    CarteiraController controller = new CarteiraController(dao);
    CarteiraView view = new CarteiraView(controller);
    
    // 3. Controladores e Views do Relatório
    RelatorioController relatorioController = new RelatorioController(dao, movDao);
    RelatorioView relatorioView = new RelatorioView(relatorioController);
    
    relatorioView.exibirMenu();
    view.exibirMenu();   
}
}
*/

//===== Main com valores de teste antes de colocar no bd =====
import java.math.BigDecimal;
import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {
        CarteiraDAO dao = new CarteiraDAOMemoria();
        MovimentacaoDAO movDao = new MovimentacaoDAOMemoria();

        popularDados(dao, movDao);

        RelatorioController relatorioController = new RelatorioController(dao, movDao);
        RelatorioView relatorioView = new RelatorioView(relatorioController);

        relatorioView.exibirMenu();
    }

    private static void popularDados(CarteiraDAO dao, MovimentacaoDAO movDao) {
        System.out.println("Inserindo dados de teste...");

        Carteira c1 = new Carteira(1, "João Silva", "XP Investimentos");
        dao.inserir(c1);

        Carteira c2 = new Carteira(2, "Maria Oliveira", "Binance");
        dao.inserir(c2);

        // Movimentações da carteira 1
        Movimentacao m1 = new Movimentacao();
        m1.setIdMovimento(1);
        m1.setIdCarteira(1);
        m1.setDataOperacao(LocalDate.of(2025, 1, 10));
        m1.setTipoMovimentacao(TipoMovimentacao.COMPRA);
        m1.setQuantidade(new BigDecimal("10.0"));
        m1.setCotacaoNaData(new BigDecimal("250.00"));
        movDao.inserir(m1);

        Movimentacao m2 = new Movimentacao();
        m2.setIdMovimento(2);
        m2.setIdCarteira(1);
        m2.setDataOperacao(LocalDate.of(2025, 1, 15));
        m2.setTipoMovimentacao(TipoMovimentacao.VENDA);
        m2.setQuantidade(new BigDecimal("5.0"));
        m2.setCotacaoNaData(new BigDecimal("260.00"));
        movDao.inserir(m2);

        Movimentacao m3 = new Movimentacao();
        m3.setIdMovimento(3);
        m3.setIdCarteira(1);
        m3.setDataOperacao(LocalDate.of(2025, 2, 1));
        m3.setTipoMovimentacao(TipoMovimentacao.COMPRA);
        m3.setQuantidade(new BigDecimal("8.0"));
        m3.setCotacaoNaData(new BigDecimal("270.00"));
        movDao.inserir(m3);

        Movimentacao m4 = new Movimentacao();
        m4.setIdMovimento(6);
        m4.setIdCarteira(1);
        m4.setDataOperacao(LocalDate.of(2025, 2, 15));
        m4.setTipoMovimentacao(TipoMovimentacao.VENDA);
        m4.setQuantidade(new BigDecimal("3.0"));
        m4.setCotacaoNaData(new BigDecimal("275.00"));
        movDao.inserir(m4);

        // Movimentações da carteira 2
        Movimentacao m5 = new Movimentacao();
        m5.setIdMovimento(4);
        m5.setIdCarteira(2);
        m5.setDataOperacao(LocalDate.of(2025, 1, 20));
        m5.setTipoMovimentacao(TipoMovimentacao.COMPRA);
        m5.setQuantidade(new BigDecimal("20.0"));
        m5.setCotacaoNaData(new BigDecimal("240.00"));
        movDao.inserir(m5);

        Movimentacao m6 = new Movimentacao();
        m6.setIdMovimento(5);
        m6.setIdCarteira(2);
        m6.setDataOperacao(LocalDate.of(2025, 2, 10));
        m6.setTipoMovimentacao(TipoMovimentacao.VENDA);
        m6.setQuantidade(new BigDecimal("10.0"));
        m6.setCotacaoNaData(new BigDecimal("280.00"));
        movDao.inserir(m6);

        System.out.println("Dados de teste foram inseridos.\n");
    }
}