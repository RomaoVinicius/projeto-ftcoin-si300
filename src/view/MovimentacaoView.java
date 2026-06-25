package src.view;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

import src.controller.MovimentacaoController;
import src.model.Movimentacao;

public class MovimentacaoView {

    private MovimentacaoController controller;
    private Scanner scanner;
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public MovimentacaoView(MovimentacaoController controller, Scanner scanner) {
        this.controller = controller;
        this.scanner = scanner;
    }

    public void exibirMenu(int idCarteira) {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n===== MOVIMENTAÇÕES =====");
            System.out.println("1. Registrar Compra");
            System.out.println("2. Registrar Venda");
            System.out.println("3. Listar Movimentações da Carteira");
            System.out.println("4. Consultar Saldo de Moedas");
            System.out.println("0. Voltar");
            System.out.print("Opção: ");

            opcao = lerInt();
            switch (opcao) {
                case 1 -> telaRegistrarCompra(idCarteira);
                case 2 -> telaRegistrarVenda(idCarteira);
                case 3 -> telaListar(idCarteira);
                case 4 -> telaSaldo(idCarteira);
                case 0 -> System.out.println("Voltando...");
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    private void telaRegistrarCompra(int idCarteira) {
        System.out.println("\n--- Registrar Compra ---");
        LocalDate data = lerData();
        if (data == null) return;
        BigDecimal quantidade = lerBigDecimal("Quantidade de moedas: ");
        if (quantidade == null) return;

        boolean sucesso = controller.registrarCompra(idCarteira, data, quantidade);
        if (sucesso) {
            System.out.println("Compra registrada com sucesso!");
        }
    }

    private void telaRegistrarVenda(int idCarteira) {
        System.out.println("\n--- Registrar Venda ---");
        LocalDate data = lerData();
        if (data == null) return;
        BigDecimal quantidade = lerBigDecimal("Quantidade de moedas: ");
        if (quantidade == null) return;

        boolean sucesso = controller.registrarVenda(idCarteira, data, quantidade);
        if (sucesso) {
            System.out.println("Venda registrada com sucesso!");
        }
    }

    private void telaListar(int idCarteira) {
        System.out.println("\n--- Movimentações da Carteira ---");
        List<Movimentacao> lista = controller.listarPorCarteira(idCarteira);
        if (lista.isEmpty()) {
            System.out.println("Nenhuma movimentação encontrada.");
            return;
        }
        for (Movimentacao m : lista) {
            System.out.printf("ID: %d | %s | %s | Qtd: %s | Cotação: %s | Total: %s%n",
                    m.getIdMovimento(),
                    m.getDataOperacao().format(FORMATO_DATA),
                    m.getTipoMovimentacao().getCodigo() == 'C' ? "COMPRA" : "VENDA",
                    m.getQuantidade(),
                    m.getCotacaoNaData(),
                    m.getValorFinanceiro());
        }
    }

    private void telaSaldo(int idCarteira) {
        BigDecimal saldo = controller.calcularSaldo(idCarteira);
        System.out.printf("\nSaldo atual de moedas: %s%n", saldo);
    }

    // --- Helpers de leitura ---

    private LocalDate lerData() {
        System.out.print("Data (dd/MM/yyyy): ");
        try {
            return LocalDate.parse(scanner.nextLine().trim(), FORMATO_DATA);
        } catch (DateTimeParseException e) {
            System.out.println("Data inválida.");
            return null;
        }
    }

    private BigDecimal lerBigDecimal(String mensagem) {
        System.out.print(mensagem);
        try {
            return new BigDecimal(scanner.nextLine().trim().replace(",", "."));
        } catch (NumberFormatException e) {
            System.out.println("Valor inválido.");
            return null;
        }
    }

    private int lerInt() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}