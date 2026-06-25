package src.controller;
// Responsável por gerenciar o fluxo de telas de ajuda e créditos do sistema.

import src.view.AjudaView;

public class AjudaController {

    private AjudaView view;

    public AjudaController() {
        this.view = new AjudaView();
    }

    public void iniciar() {
        int opcao;
        do {
            opcao = view.exibirMenuAjuda();

            switch (opcao) {
                case 1:
                    view.exibirTextoAjuda();
                    break;
                case 2:
                    view.exibirCreditos();
                    break;
                case 0:
                    System.out.println("Retornando ao Menu Principal...");
                    break;
                default:
                    view.exibirMensagemErro("Opção inválida, tente novamente :)");
            }
        } while (opcao != 0);
    }
}