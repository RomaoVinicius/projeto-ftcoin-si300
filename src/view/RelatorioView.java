package src.view;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

import src.controller.RelatorioController;
import src.model.Movimentacao;

public class RelatorioView {
    private final RelatorioController controller;
    private final Scanner scanner;

    public RelatorioView(RelatorioController controller) {
        this.controller = controller;
        this.scanner = new Scanner(System.in);
    }

    public void exibirMenu() {
        int opcao = 0;
        do {
            System.out.println("\n=========|| MENU RELATÓRIOS ||=========");
            System.out.println();
            System.out.println("1. Listar carteiras (Ordenadas por ID)");
            System.out.println("2. Listar carteiras (Ordenadas por Nome)");
            System.out.println("3. Exibir saldo atual de uma carteira");
            System.out.println("4. Exibir histórico de movimentações");
            System.out.println("5. Exibir ganho ou perda total");
            System.out.println("6. Voltar ao menu principal");
            System.out.println("\n=======================================");
            System.out.println();
            System.out.print("Escolha uma opção: ");

            try {
                opcao = scanner.nextInt();
                scanner.nextLine(); // Limpa enter 

                switch (opcao) {
                    case 1 -> {
                        System.out.println("\n------| CARTEIRAS POR ID |------");
                        controller.listarOrdenadasPorId().forEach(c -> System.out.println(c.toString()));
                    }
                    case 2 -> {
                        System.out.println("\n------| CARTEIRAS POR NOME |------");
                        controller.listarOrdenadasPorNome().forEach(c -> System.out.println(c.toString()));
                    }
                    case 3 -> {
                        System.out.print("Digite o ID da carteira: ");
                        int id = scanner.nextInt();
                        System.out.println("Saldo de moedas: " + controller.calcularSaldoAtual(id));
                    }
                    case 4 -> {
                        System.out.print("Digite o ID da carteira: ");
                        int id = scanner.nextInt();
                        System.out.println("\n------| HISTÓRICO DE MOVIMENTAÇÕES |------");
                        List<Movimentacao> hist = controller.obterHistoricoOrdenado(id);
                        if (hist.isEmpty()) {
                            System.out.println("Nenhuma movimentação encontrada.");
                        } else {
                            hist.forEach(m -> System.out.println(m.toString()));
                        }
                    }
                    case 5 -> {
                        System.out.println("\n-----| GANHO OU PERDA |-----");
                        for (Carteira c : controller.listarOrdenadasPorId()) {
                            BigDecimal resultado = controller.calcularGanhoOuPerda(c.getIdentificador());
                            String status = resultado.compareTo(BigDecimal.ZERO) >= 0 ? "[LUCRO]" : "[PREJUÍZO]";
                            System.out.printf("ID %d | Titular: %s | Balanço: R$ %s %s\n", 
                                c.getIdentificador(), c.getNomeTitular(), resultado, status);
                        }
                    }
                    case 6 -> System.out.println("Voltando...");
                    default -> System.out.println("Opção inválida!");
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
                scanner.nextLine(); // Evita o loop infinito 
            }
        } while (opcao != 6);
    }
}
