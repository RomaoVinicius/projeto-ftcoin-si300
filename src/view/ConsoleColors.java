package src.view;

public final class ConsoleColors {
    public static final String RESET = "\u001B[0m";
    public static final String BOLD = "\u001B[1m";
    public static final String UNDERLINE = "\u001B[4m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String CYAN = "\u001B[36m";

    private ConsoleColors() {
        // Utility class for console color formatting.
        // Classe utilitária para formatação de cores no console.
    }

    public static String colorize(String text, String color) {
        return color + text + RESET;
    }
}
