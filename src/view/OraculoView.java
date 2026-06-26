package src.view;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import src.controller.OraculoController;
import src.model.Cotacao;

public class OraculoView {

    private final OraculoController controller;
    private final Scanner scanner;

    private static final DateTimeFormatter FORMATO =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public OraculoView(OraculoController controller, Scanner scanner) {
        this.controller = controller;
        this.scanner = scanner;
    }

    public void consultarCotacao() {

        System.out.print("Digite a data (dd/MM/yyyy): ");

        try {

            LocalDate data = LocalDate.parse(scanner.nextLine(), FORMATO);

            Cotacao cotacao = controller.consultar(data);

            if (cotacao == null) {
                System.out.println("Não existe cotação para esta data.");
                return;
            }

            System.out.println();
            System.out.println("\n========== ORÁCULO ==========");
            System.out.println("Data: " + data.format(FORMATO));
            System.out.println(String.format("Cotação: R$ %.2f", cotacao.getValor()));
            System.out.println("=============================");

        } catch (DateTimeParseException e) {
            System.out.println("Data inválida.");
        }
    }
}
