package ui;

import java.util.Scanner;

/**
 * The Cli class is used to present a Command Line Interface menu to the user.
 * The user can choose an option or end the program.
 *
 * @author Sonja Lechner
 * @version 1.0 19.11.2022
 */

public class Cli {
    Scanner scan;

    /**
     * class constructor
     */
    public Cli(){
        this.scan = new Scanner(System.in);
    }

    /**
     * Displays the menu.
     */
    private void showMenu(){
        System.out.println("---------------------KURS MANAGEMENT---------------------------\n");
        System.out.println("Bitte Geben Sie der Zahl ihre Wahl ein\n");
        System.out.println("\t1: Kurs eingeben \n\t2: Alle Kurse anzeigen \n\tx: Program beenden");
    }

    /**
     * Displays an error message in case the user enters invalid information.
     */
    private void inputError(){
        System.out.println("Falsche Eingabe!\n" +
                "Bitte geben Sie nur 1, 2 oder x ein: ");
    }

    /**
     * Initializes the Command Line Interface and manages its behaviour.
     */
    public void start(){
        String input = "-";
        while (!input.equals("x")){ //Menu is repeatedly shown unless x(to end the program) is entered. This allows the user to make more than 1 choice in succession.
            showMenu();
            input = scan.nextLine();

            switch (input) {
                case "1" -> System.out.println("Kurseingabe\n");
                case "2" -> System.out.println("Alle Kurse anzeigen\n");
                case "x" -> System.out.println("Auf Wiedersehen");
                default -> inputError();
            }
        }
        scan.close();
    }
}
