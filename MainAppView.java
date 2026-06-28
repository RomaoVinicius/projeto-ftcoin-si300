
import java.util.Scanner;

import src.controller.AjudaController;
import src.controller.CarteiraController;
import src.controller.MovimentacaoController;
import src.controller.RelatorioController;
import src.dao.CarteiraDAO;
import src.dao.CotacaoDAO;
import src.dao.MovimentacaoDAO;
import src.dao.memoria.CarteiraDAOMemoria;
import src.dao.memoria.CotacaoDAOMemoria;
import src.dao.memoria.MovimentacaoDAOMemoria;
import src.view.CarteiraView;
import src.view.MovimentacaoView;
import src.view.RelatorioView;

public class MainAppView {

    public static void main(String[] args) {
        MainAppView menuPrincipal = new MainAppView();
        menuPrincipal.exibirMenuPrincipal();
    }

    public void exibirMenuPrincipal() {
        Scanner scanner = new Scanner(System.in);
        CarteiraDAO carteiraDAO = new CarteiraDAOMemoria();
        CotacaoDAO cotacaoDAO = new CotacaoDAOMemoria();
        MovimentacaoDAO movimentacaoDAO = new MovimentacaoDAOMemoria();
        int opcao = -1;

        do {
            System.out.println("\n=======================================");
            System.out.println("          SISTEMA FTCOIN - SI300       ");
            System.out.println("=======================================");
            System.out.println("1. Acessar Carteira");
            System.out.println("2. Movimentações");
            System.out.println("3. Emitir Relatórios");
            System.out.println("4. Ajuda");
            System.out.println("0. Sair do Programa");
            System.out.println("=======================================");
            System.out.print("Escolha uma opção: ");

            // Validação simples para o programa não quebrar se o usuário digitar uma letra
            if (scanner.hasNextInt()) {
                opcao = scanner.nextInt();
                scanner.nextLine(); // Limpa o buffer do teclado
            } else {
                System.out.println("Opção inválida! Digite apenas números.");
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
                    System.out.print("Digite o ID da sua Carteira para acessar as movimentações: ");
                    int id = scanner.nextInt();
                    scanner.nextLine(); // Limpa o buffer

                    MovimentacaoController movimentacaoController = new MovimentacaoController(movimentacaoDAO, cotacaoDAO); 
                    MovimentacaoView movimentacao = new MovimentacaoView(movimentacaoController, scanner);
                    movimentacao.exibirMenu(id);
                    break;

                case 3:
                    RelatorioController relatorioController = new RelatorioController(carteiraDAO, movimentacaoDAO);
                    RelatorioView relatorio = new RelatorioView(relatorioController);
                    relatorio.exibirMenu();
                    break;

                case 4:
                    AjudaController ajudaController = new AjudaController();
                    ajudaController.iniciar(); 
                    break;

                case 0:
                    System.out.println("Encerrando o sistema FTCoin. Até logo!");
                    break;

                default:
                    System.out.println("Opção inválida! Tente novamente.");
                    break;
            }

        } while (opcao != 0);

        scanner.close();
    }
}