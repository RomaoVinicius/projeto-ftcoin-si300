import java.util.InputMismatchException;
import java.util.Scanner;
//testando
public class CarteiraView {
    private final CarteiraController controller;
    private final Scanner scanner;

    public CarteiraView(CarteiraController controller) {
        this.controller = controller;
        this.scanner = new Scanner(System.in);
    }

    public void exibirMenu() {
        int opcao = 0;
        do {
            System.out.println("\n=== SUBMENU: CARTEIRA ===");
            System.out.println("1. Incluir carteira");
            System.out.println("2. Consultar carteira");
            System.out.println("3. Editar carteira");
            System.out.println("4. Excluir carteira");
            System.out.println("5. Voltar");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = scanner.nextInt();
                scanner.nextLine();

                switch (opcao) {
                    case 1 -> incluir();
                    case 2 -> consultar();
                    case 3 -> editar();
                    case 4 -> excluir();
                    case 5 -> System.out.println("Saindo do menu de carteira...");
                    default -> System.out.println("Opção inválida! Tente novamente.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Erro: Por favor, digite apenas números.");
                scanner.nextLine();
            }
        } while (opcao != 5);
    }

    private void incluir() {
        try {
            System.out.print("Digite o ID da carteira: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Digite o nome do titular: ");
            String nome = scanner.nextLine();

            System.out.print("Digite a corretora: ");
            String corretora = scanner.nextLine();

            controller.incluirCarteira(id, nome, corretora);
            System.out.println("[SUCESSO] Carteira cadastrada!");
        } catch (IllegalArgumentException e) {
            System.out.println("[ERRO] " + e.getMessage());
        }
    }

    private void consultar() {
        try {
            System.out.print("Digite o ID da carteira para buscar: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            Carteira carteira = controller.consultarCarteira(id);
            System.out.println("[RESULTADO] " + carteira.toString());
        } catch (IllegalArgumentException e) {
            System.out.println("[ERRO] " + e.getMessage());
        }
    }

    private void editar() {
        try {
            System.out.print("Digite o ID da carteira que deseja editar: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Digite o NOVO nome do titular: ");
            String novoNome = scanner.nextLine();

            System.out.print("Digite a NOVA corretora: ");
            String novaCorretora = scanner.nextLine();

            controller.editarCarteira(id, novoNome, novaCorretora);
            System.out.println("[SUCESSO] Carteira atualizada!");
        } catch (IllegalArgumentException e) {
            System.out.println("[ERRO] " + e.getMessage());
        }
    }

    private void excluir() {
        try {
            System.out.print("Digite o ID da carteira que deseja excluir: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            controller.excluirCarteira(id);
            System.out.println("[SUCESSO] Carteira excluída!");
        } catch (IllegalArgumentException e) {
            System.out.println("[ERRO] " + e.getMessage());
        }
    }
}
