package src.view;
import java.util.Scanner;

//Classe responsável pela exibição de informações de ajuda e créditos.

public class AjudaView {

    private Scanner scanner;

    public AjudaView() {
        this.scanner = new Scanner(System.in);
    }

    public int exibirMenu() {
        System.out.println("\n=======================================");
            System.out.println("          MENU DE AJUDA       ");
            System.out.println("=======================================");
        System.out.println("1. Como usar o sistema");
        System.out.println("2. Créditos do sistema");
        System.out.println("0. Voltar ao menu principal");
        System.out.print("Escolha uma opção: ");
        
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1; // Retorna opção inválida caso o usuário digite letras
        }
    }

    public void exibirTextoAjuda() {
        System.out.println("\n-----| COMO USAR O FT COIN |-----");
        System.out.println("O FT Coin é um sistema de acompanhamento de carteira de moeda virtual.");
        System.out.println("\nFUNCIONALIDADES:");
        System.out.println("- CARTEIRA: inclua, consulte, edite ou exclua carteiras.");
        System.out.println("- MOVIMENTAÇÃO: registre compras (C) e vendas (V) vinculadas a uma carteira.");
        System.out.println("- RELATÓRIOS: acompanhe o saldo, o histórico e o ganho/perda conforme a cotação do Oráculo.");
        System.out.println("- ORÁCULO: consulte a cotação de uma data específica.");
        System.out.println("\nDICA: cadastre uma carteira antes de registrar movimentações.");
        System.out.println("---------------------------\n");
        pausar();
    }

    public void exibirCreditos() {
        System.out.println("\n-----| CRÉDITOS DO SISTEMA |-----");
        System.out.println("Sistema: FT Coin");
        System.out.println("Versão: 1.0");
        System.out.println("Data: Junho de 2026");
        System.out.println("Desenvolvido por: Grupo 01");
        System.out.println("Integrantes: Ana Julia Maximo, Bárbara Helóra, Beatriz Moreira, Raíssa Souza, Thiago Yuiti, Vinicius Romão e Yasmin Caetano");
        System.out.println("Copyright (c) 2026 - Faculdade de Tecnologia - Unicamp");
        System.out.println("---------------------------\n");
        pausar();
    }

    public void exibirMensagemErro(String mensagem) {
        System.out.println("\n[ERRO] " + mensagem);
    }

    private void pausar() {
        System.out.print("Pressione ENTER para continuar...");
        scanner.nextLine();
    }
}