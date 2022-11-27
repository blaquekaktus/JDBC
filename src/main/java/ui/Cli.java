package ui;

import dataaccess.MyCourseRepository;
import dataaccess.DatabaseException;
import domain.Course;
import domain.CourseType;
import domain.InvalidValueException;


import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.sql.Date;

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
                case "1" -> addCourse();
                case "2" -> showAllCourses();
                case "3" -> showCourseDetails();
                case "4" -> updateCourseDetails();
                case "x" -> System.out.println("Auf Wiedersehen");
                default -> inputError();
            }
        }
        scan.close();                   //closes the scanner
    }

    /**
     * adds a new Course object to the database
     * @throws DatabaseException
     * @throws IllegalArgumentException
     */
    private void addCourse() throws DatabaseException, IllegalArgumentException{
        String name, description;                               //Temporary Variables for each Object Property, used to store the information entered by the user
        int hours;
        Date beginDate, endDate;
        CourseType courseType;


        try{
            System.out.println("Bitte alle Kursdaten angeben:");
            System.out.println("Name: ");
            name = scan.nextLine();
            if(name.equals("")) throw new IllegalArgumentException("Eingabe darf nicht leer sein!"); //UI (Client-Side) Validation in order to ensure that the data entered is database consistent.
            System.out.println("Description: ");
            description = scan.nextLine();
            if(description.equals("")) throw new IllegalArgumentException("Eingabe darf nicht leer sein!");
            System.out.println("Hours: ");
            hours = Integer.parseInt(scan.nextLine());
            // Text Input (String) der CLI  converted to type 'int' through Integer.parseInt(number) method
            // Through consistent use of 'scan.nextLine' instead of 'scan.nextInt' etc., the risks of exceptions occurring due to the user pressing the enter key twice is eliminated
            if(hours<=0) throw new IllegalArgumentException("Eingabe muss mindestens 1 sein!");
            System.out.println("Startdatum (YYYY-MM-DD): ");
            beginDate = Date.valueOf(scan.nextLine());
            System.out.println("Enddatum (YYYY-MM-DD): ");
            endDate = Date.valueOf(scan.nextLine());
            System.out.println("KUrs (ZA,BF,FF,OE) ");
            courseType =CourseType.valueOf(scan.nextLine());

            Optional<Course> optionalCourse = repo.insert(
                    new Course(name, description, hours, beginDate, endDate, courseType)
            );
            if(optionalCourse.isPresent()){
                System.out.println("Kurs angelegt: " + optionalCourse.get());
            }else{
                System.out.println("Kurs könnte nicht angelegt werden");
            }


        }
        catch(IllegalArgumentException illegalArgumentException){
            // Handles exceptions thrown when the user enters one or more incorrect or illegal arguments. Returns an error message with the details of the exception.
            System.out.println("Eingabefehler: " + illegalArgumentException.getMessage());
        }
        catch(InvalidValueException invalidValueException){
            System.out.println("Kursdaten nicht korrekt eingegeben: " + invalidValueException.getMessage());
            // Handles exceptions thrown when the user enters one or more invalid value. Returns an error message with the details of the exception.
        }
        catch(DatabaseException databaseException){
            System.out.println("Datenbankfehler beim Einfügen: " + databaseException.getMessage());
            // Handles exceptions thrown when the user SQL Database exceptions occurs. Returns an error message with the details of the exception.
        }
        catch(Exception exception){
            System.out.println("Unbekannter Fehler beim Einfügen: " + exception.getMessage());
            // Handles any other exceptions thrown. Returns an error message with the details of the exception.
        }
    }

    /**
     * Displays all the courses in the database
     * @throws DatabaseException
     */
    private void showAllCourses() throws DatabaseException {
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
        }catch(DatabaseException e){
            System.out.println("Datenbankfehler bei Anzeige aller Kurse: " + e.getMessage());
        }catch(Exception exception){
            System.out.println("Unbekannter Fehler ist bei anzeigen alle Kurse: " + exception.getMessage());
        }
    }

    /**
     * Prints the details of the course corresponding to the ID entered by the user on the CLI
     * If the course isn't found in the database an error message is shown
     */
    private void showCourseDetails() throws DatabaseException {
        System.out.println("Für welchen Kurs möchten Sie die Kursdetails anzeigen?");
        Long courseId = Long.parseLong(scan.nextLine());
        try{
            Optional<Course>courseOptional = repo.getById(courseId);
            if(courseOptional.isPresent()){
                System.out.println(courseOptional.get());
            }else{
                System.out.println("Kurs mit der ID " + courseId + " nicht gefunden");
            }

        }catch(DatabaseException databaseException){
            System.out.println("Datenbankfehler bei Kurs-Detailanzeigen " + databaseException.getMessage());

        }catch(Exception exception){
            System.out.println("Unbekannter Fehler bei Kurs-Detailanzeigen " + exception.getMessage());
        }
    }

    private void updateCourseDetails() throws DatabaseException {
        System.out.println("Für welchen Kurs möchten Sie die Kursdetails ändern?");
        Long courseId = Long.parseLong(scan.nextLine());
        try{
            Optional<Course>courseOptional = repo.getById(courseId);
            if(courseOptional.isEmpty()){
                System.out.println("Kurs mit der ID " + courseId + " nicht gefunden");
            }else{
                Course course  = courseOptional.get();
                System.out.println("Anderung für folgenden Kurs: ");
                System.out.println(course);

                String name, description, hours, startDate, endDate, courseType;
                System.out.println("Bitte neue Kursdaten angeben (Enter,falls keine Änderung gewünscht ist): ");
                System.out.println("Name: ");
                name = scan.nextLine();
                System.out.println("Beschreibung: ");
                description = scan.nextLine();
                System.out.println("Stundenanzahl: ");
                hours = scan.nextLine();
                System.out.println("Startdatum (YYYY-MM-DD): ");
                startDate = scan.nextLine();
                System.out.println("Enddatum (YYYY-MM-DD): ");
                endDate= scan.nextLine();
                System.out.println("Kurstyp (ZA/BF/FF/OE");
                courseType = scan.nextLine();

                Optional<Course>optionalCourseUpdated = repo.update(
                        new Course(
                                course.getID(),
                                name.equals("") ? course.getName() : name,
                                description.equals("")? course.getDescription() : description,
                                hours.equals("")?course.getHours() : Integer.parseInt(hours),
                                startDate.equals("") ? course.getBeginDate() : Date.valueOf(startDate),
                                endDate.equals("") ? course.getEndDate() : Date.valueOf(endDate),
                                courseType.equals("") ? course.getCourseType() : CourseType.valueOf(courseType)
                        ));
                optionalCourseUpdated.ifPresentOrElse(
                        (c)-> System.out.println("Kurs aktualisiert: " + c),
                        () -> System.out.println("Kurs konnte nicht aktualisiert werden!")
                );

            }

        }catch(DatabaseException databaseException){
            System.out.println("Datenbankfehler bei Kurs-Detailanzeigen: " + databaseException.getMessage());

        }catch(Exception exception){
            System.out.println("Unbekannter Fehler bei Kurs-Detailanzeigen: " + exception.getMessage());
        }
    }
}
