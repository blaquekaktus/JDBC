package ui;

import java.util.Scanner;

public class Cli {
    Scanner scan;

    public Cli(){
        this.scan = new Scanner(System.in);
    }

    private void showMenu(){
        System.out.println("---------------------KURS MANAGEMENT---------------------------\n");
        System.out.println("Bitte Geben Sie der Zahl ihre Wahl ein\n");
        System.out.println("\t1: Kurs eingeben \n\t2: Alle Kurse anzeigen \n\tx: Program beenden");
    }

    public void start(){
        String input = "-";
        while (!input.equals("x")){
            showMenu();
            input = scan.nextLine();

            switch(input){
                case "1":
                    System.out.println("Kurseingabe\n");
                    break;
                case "2":
                    System.out.println("Alle Kurse anzeigen\n");
                    break;
                case "x":
                    System.out.println("Auf Wiedersehen");
                    break;
                default:
                    System.out.println("Falsche Eingabe!\n" +
                            "Bitte geben Sie nur 1, 2 oder X ein: ");
            }
        }
        scan.close();
    }
}
