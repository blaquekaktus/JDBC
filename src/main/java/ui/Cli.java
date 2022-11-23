package ui;

import dataaccess.MyCourseRepository;
import dataaccess.MySqlDatabaseException;
import domain.Course;


import java.util.List;
import java.util.Optional;
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
    MyCourseRepository repo;


    /**
     * class constructor
     * @param repo MyCourseRepository A MyCourseRepository Object
     */
    public Cli(MyCourseRepository repo){
        this.scan = new Scanner(System.in);
        this.repo = repo;
    }

    /**
     * Displays the menu.
     */
    private void showMenu(){
        System.out.println("\n---------------------KURS MANAGEMENT---------------------------\n");
        System.out.println("Bitte Geben Sie der Zahl ihre Wahl ein\n");
        System.out.println("\t1: Kurs eingeben \n\t2: Alle Kurse anzeigen \n\t3: Kursdetails anzeigen \n\tx: Program beenden");
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
        while (!input.equals("x")){     //Menu is repeatedly shown unless x(to end the program) is entered. This allows the user to make more than 1 choice in succession.
            showMenu();
            input = scan.nextLine();

            switch (input) {
                case "1" -> System.out.println("Kurs Eingeben");
                case "2" -> showAllCourses();
                case "3" -> showCourseDetails();
                case "x" -> System.out.println("Auf Wiedersehen");
                default -> inputError();
            }
        }
        scan.close();                   //closes the scanner
    }

    /**
     * Initializes the Command Line Interface and manages its behaviour.
     */
    private void showAllCourses() throws MySqlDatabaseException{
        List<Course> list = null;

        list = repo.getAll();

        try {
            if (list.size() > 0) {
                for (Course course : list) {
                    System.out.println(course);
                }
            } else {
                System.out.println("Keine Kurse vorhanden!");
            }
        }catch(MySqlDatabaseException e){
            System.out.println("Datenbankfehler bei Anzeige aller Kurse: " + e.getMessage());
        }catch(Exception exception){
            System.out.println("Unbekannter Fehler ist bei anzeigen alle Kurse: " + exception.getMessage());
        }
    }

    private void showCourseDetails(){
        System.out.println("Für welchen Kurs möchten Sie die kursdetails anzeigen?");
        Long courseId = Long.parseLong(scan.nextLine());
        try{
            Optional<Course>courseOptional = repo.getById(courseId);
            if(courseOptional.isPresent()){
                System.out.println(courseOptional.get());
            }else{
                System.out.println("Kurs mit der ID " + courseId + " nicht gefunden");
            }

        }catch(MySqlDatabaseException databaseException){
            System.out.println("Datenbankfehler bei Kurs-Detailanzeigen " + databaseException.getMessage());

        }catch(Exception exception){
            System.out.println("Unbekannter Fehler bei Kurs-Detailanzeigen " + exception.getMessage());
        }
    }
}
