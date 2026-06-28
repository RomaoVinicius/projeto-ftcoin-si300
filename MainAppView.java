
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Random;
import java.util.Scanner;

import src.controller.AjudaController;
import src.controller.CarteiraController;
import src.controller.MovimentacaoController;
import src.controller.RelatorioController;
import src.dao.CarteiraDAO;
import src.dao.CotacaoDAO;
import src.dao.MovimentacaoDAO;
import src.dao.mariadb.CarteiraMariaDAO;
import src.dao.mariadb.CotacaoMariaDAO;
import src.dao.mariadb.MovimentacaoMariaDAO;
import src.model.Cotacao;
import src.view.CarteiraView;
import src.view.ConsoleColors;
import src.view.MovimentacaoView;
import src.view.RelatorioView;

public class MainAppView {

    public static void main(String[] args) {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver do MariaDB não encontrado: " + e.getMessage());
            return;
        }

        // Pre-populate quotations for the oracle feature.
        // Pré-popular cotações para o recurso do oráculo.
        CotacaoDAO cotacaoDAO = new CotacaoMariaDAO();

        LocalDate data = LocalDate.of(2026, 1, 1);
        LocalDate fim = LocalDate.of(2026, 12, 31);

        Random random = new Random();

        // Initial quote value.
        // Valor inicial da cotação.
        BigDecimal valor = new BigDecimal("350.00");

        if (cotacaoDAO.listarTodas().isEmpty()) {
            for (LocalDate d = data; !d.isAfter(fim); d = d.plusDays(1)) {

                // Daily variation between -3% and +3%.
                // Variação diária entre -3% e +3%.
                double variacao = (random.nextDouble() * 0.06) - 0.03;

                valor = valor.multiply(BigDecimal.valueOf(1 + variacao))
                            .setScale(2, RoundingMode.HALF_UP);

                // Avoid very low values.
                // Evita valores muito baixos.
                if (valor.compareTo(new BigDecimal("150.00")) < 0) {
                    valor = new BigDecimal("150.00");
                }

                cotacaoDAO.inserir(new Cotacao(d, valor));
            }
        }




        MainAppView menuPrincipal = new MainAppView();
        menuPrincipal.exibirMenuPrincipal();
    }

    private void consultarOraculo(Scanner scanner, CotacaoDAO cotacaoDAO) {
        System.out.print("Digite a data (dd/MM/yyyy): ");
        String textoData = scanner.nextLine().trim();

        try {
            LocalDate data = LocalDate.parse(textoData, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            Cotacao cotacao = cotacaoDAO.consultar(data);
            if (cotacao == null) {
                System.out.println("Nenhuma cotação encontrada para esta data.");
            } else {
                System.out.printf("Cotação do dia %s: R$ %s%n", data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), cotacao.getValor());
            }
        } catch (DateTimeParseException e) {
            System.out.println("Data inválida. Use o formato dd/MM/yyyy.");
        }
    }

    public void exibirMenuPrincipal() {
        Scanner scanner = new Scanner(System.in);
        CarteiraDAO carteiraDAO = new CarteiraMariaDAO();
        CotacaoDAO cotacaoDAO = new CotacaoMariaDAO();
        MovimentacaoDAO movimentacaoDAO = new MovimentacaoMariaDAO();
        int opcao = -1;

        do {
            System.out.println("\n=======================================");
            System.out.println("          SISTEMA FTCOIN - SI300       ");
            System.out.println("=======================================");
            System.out.println("1. Acessar Carteira");
            System.out.println("2. Movimentações");
            System.out.println("3. Emitir Relatórios");
            System.out.println("4. Ajuda");
            System.out.println("5. Consultar Oráculo");
            System.out.println("0. Sair do Programa");
            System.out.println("=======================================");
            System.out.print("Escolha uma opção: ");

            // Validação simples para o programa não quebrar se o usuário digitar uma letra
            if (scanner.hasNextInt()) {
                opcao = scanner.nextInt();
                scanner.nextLine(); // Limpa o buffer do teclado
            } else {
                System.out.println(ConsoleColors.colorize("Opção inválida! Digite apenas números.", ConsoleColors.RED));
                scanner.nextLine(); // Limpa o texto incorreto
                continue;
            }

            System.out.println(); // Linha em branco para organizar o visual

            switch (opcao) {
                case 1:
                    CarteiraController carteiraController = new CarteiraController(carteiraDAO); 
                    CarteiraView carteira = new CarteiraView(carteiraController); 
                    carteira.exibirMenu(); 
                    break;

                case 2:
                    System.out.print(ConsoleColors.colorize("Digite o ID da sua Carteira para acessar as movimentações: ", ConsoleColors.YELLOW));
                    int id = scanner.nextInt();
                    scanner.nextLine(); // Limpa o buffer

                    MovimentacaoController movimentacaoController = new MovimentacaoController(movimentacaoDAO, cotacaoDAO); 
                    MovimentacaoView movimentacao = new MovimentacaoView(movimentacaoController, scanner);
                    movimentacao.exibirMenu(id);
                    break;

                case 3:
                    RelatorioController relatorioController = new RelatorioController(carteiraDAO, movimentacaoDAO, cotacaoDAO);
                    RelatorioView relatorio = new RelatorioView(relatorioController);
                    relatorio.exibirMenu();
                    break;

                case 4:
                    AjudaController ajudaController = new AjudaController();
                    ajudaController.iniciar(); 
                    break;

                case 5:
                    consultarOraculo(scanner, cotacaoDAO);
                    break;

                case 0:
                    System.out.println(ConsoleColors.colorize("Encerrando o sistema FTCoin. Até logo!", ConsoleColors.GREEN));
                    break;

                default:
                    System.out.println(ConsoleColors.colorize("Opção inválida! Tente novamente.", ConsoleColors.RED));
                    break;
            }

        } while (opcao != 0);

        scanner.close();
    }
}