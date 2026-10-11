package app.view;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Ayudante para leer datos por consola sin repetir código en cada view.
 * Se crea UNA sola vez en AppConfig y se comparte (inyección de dependencias).
 */
public class ConsoleInput {

    private final Scanner scanner;

    public ConsoleInput(Scanner scanner) {
        this.scanner = scanner;
    }

    public String readText(String label) {
        System.out.print(label + ": ");
        return scanner.nextLine().trim();
    }

    public int readOption(String label, int min, int max) {
        while (true) {
            String text = readText(label);
            try {
                int value = Integer.parseInt(text);
                if (value >= min && value <= max) {
                    return value;
                }
            } catch (NumberFormatException ignored) {
                // se vuelve a pedir abajo
            }
            System.out.println("Opción no válida. Escriba un número entre " + min + " y " + max + ".");
        }
    }

    public LocalDate readDate(String label) {
        while (true) {
            String text = readText(label);
            try {
                return LocalDate.parse(text);
            } catch (DateTimeParseException e) {
                System.out.println("Fecha no válida. Use el formato AAAA-MM-DD, por ejemplo 1998-05-21.");
            }
        }
    }

    public boolean readYesNo(String label) {
        while (true) {
            String text = readText(label).toLowerCase();
            if (text.equals("s") || text.equals("si") || text.equals("sí")) {
                return true;
            }
            if (text.equals("n") || text.equals("no")) {
                return false;
            }
            System.out.println("Responda s o n.");
        }
    }

    public <E extends Enum<E>> E readEnum(String label, E[] values) {
        System.out.println(label + ":");
        for (int i = 0; i < values.length; i++) {
            System.out.println("  " + (i + 1) + ". " + values[i]);
        }
        int option = readOption("Seleccione una opción", 1, values.length);
        return values[option - 1];
    }

    public void showTitle(String title) {
        System.out.println();
        System.out.println("========== " + title + " ==========");
    }
}
