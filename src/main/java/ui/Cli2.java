package ui;

import dataaccess.*;
import domain.Course;
import domain.Student;
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

public class Cli2 {
    Scanner scan;
    MyCourseRepository repo;
    MyStudentRepository repo2;

    /**
     * class constructor
     * @param repo MyCourseRepository A MySqlCourseRepository Object
     */
    public Cli2(MyCourseRepository repo){
        this.scan = new Scanner(System.in);
        this.repo = repo; //course repository object

    }

    /**
     * class constructor
     * @param repo2
     */
    public Cli2(MyStudentRepository repo2){
        this.scan = new Scanner(System.in);
        this.repo2 = repo2; //student repository object

    }

    /**
     * Starts the CLI
     */
    public void start() {
        String input = "-";  //Menu is repeatedly shown unless x(to end the program) is entered. This allows the user to make more than 1 choice in succession.
            showMenu();
            input = (scan.nextLine().toLowerCase());
            switch (input) {
                case "k" -> {
                    kurs();
                    break;
                }
                case "s" -> {
                    student();
                    break;
                }
                case "x" -> {
                    System.out.println("Auf Wiedersehen");
                    break;
                }
                default -> inputError();
            }
        scan.close();
    }



    /**
     * Displays the menu.
     */

    private void showMenu(){
        System.out.println("\n---------------------DATENBANK MANAGEMENT---------------------------\n");
        System.out.println("\nWelche Datenbank möchten Sie zugreifen?\nBitte Geben Sie ihr Wahl unten ein\n");
        System.out.println(
                """
                                \tk: Kurse\s                                    
                                \ts: Student:innen\s
                                \tx: Program beenden\n
                """
        );
    }

    /**
     * Initializes the Command Line Interface and manages its behaviour.
     */
    private void showKursMenu(){
        System.out.println("\n---------------------------KURSMANAGEMENT---------------------------\n");
        System.out.println("\nWas möchten Sie jetzt tun?\nBitte Geben Sie ihr Wahl unten ein\n");
        System.out.println(
                """
                        \t1: Kurs eingeben\s
                        \t2: Alle Kurse anzeigen\s
                        \t3: Kursdetails anzeigen\s
                        \t4: Kursdetails ändern\s
                        \t5: Kurs löschen\s
                        \t6: Kurs suchen beim Namen oder Beschreibung\s
                        \t7: Kurs suchen bei BeginDatum\s
                        \t8: Alle laufende Kurse anzeigen\s 
                        \tx: Program beenden\n
                """
        );
    }

    private void kurs(){
        String input = "-";
        while (!input.equals("x")) {     //Menu is repeatedly shown unless x(to end the program) is entered. This allows the user to make more than 1 choice in succession.
            showKursMenu();
           input= (scan.nextLine().toLowerCase());
            switch (input) {
                case "1" -> addCourse();
                case "2" -> showAllCourses();
                case "3" -> showCourseDetails();
                case "4" -> updateCourseDetails();
                case "5" -> deleteCourse();
                case "6" -> courseSearch();
                case "7" -> coursesByStart();
                case "8" -> runningCourses();
                case "x" -> System.out.println("Auf Wiedersehen");
                default -> inputError();
            }
        }
        scan.close();                   //closes the scanner
    }



    private void showStudentMenu(){
        System.out.println("\n---------------------------STUDENT:INNEN MANAGEMENT---------------------------\n");
        System.out.println("\nWas möchten Sie jetzt tun?\nBitte Geben Sie ihr Wahl unten ein\n");
        System.out.println(
                """
                        \t1: Neue Student einfugen\s
                        \t2: Alle Student:innen anzeigen\s
                        \t3: Student:innen Details anzeigen\s
                        \t4: Student:innen Details ändern\s
                        \t5: Student:innen löschen\s
                        \t6: Student:innen suchen bei Name \s
                        \t7: Student:innen suchen beim Geburtstagdatum\s
                        \t8: Student:innen suchen beim Geburtstagdatum im Spezifischen Zeitraum\s 
                        \tx: Program beenden\n
                """
        );
    }

    private void student(){
        String input = "-";
        while (!input.equals("x")) {     //Menu is repeatedly shown unless x(to end the program) is entered. This allows the user to make more than 1 choice in succession.
            showStudentMenu();
            input = (scan.nextLine().toLowerCase());
            switch(input) {
                case "1" -> addStudent();
                case "2" -> showAllStudents();
                case "3" -> showStudentDetails();
                case "4" -> updateStudentDetails();
                case "5" -> deleteStudent();
                case "6" -> studentSearch();
                case "7" -> studentsByBirthdate();
                case "8" -> studentsByBirthdateInRange();
                case "x" -> System.out.println("Auf Wiedersehen");
                default -> inputError();
            }
        }
        scan.close();                   //closes the scanner
    }
    /**
     * Displays an error message in case the user enters invalid information.
     */
    private void inputError(){
        System.out.println("Falsche Eingabe!\n" +
                "Bitte geben Sie nur die Zahlen der Menüauswahl  oder x ein: ");
    }




    /**
     * adds a new Course object to the database
     *   @throws DatabaseException
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
            System.out.println("Kurs (ZA,BF,FF,OE) ");
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
     * Displays all the courses found in the database
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

    /**
     * Allows the user to change the fields of a course entity in the database.
     *
     * @throws DatabaseException
     */
    private void updateCourseDetails() throws DatabaseException {
        System.out.println("Für welchen Kurs möchten Sie die Kursdetails ändern?");
        Long courseId = Long.parseLong(scan.nextLine());
        try {
            Optional<Course> courseOptional = repo.getById(courseId);                   //get the course by the ID
            if (courseOptional.isEmpty()) {                                             //check whether a course with the ID entered exists.
                System.out.println("Kurs mit der ID " + courseId + " nicht gefunden");   // if the course doesn't exist then the user is informed
            } else {
                Course course = courseOptional.get();
                System.out.println("Anderung für folgenden Kurs: ");
                System.out.println(course);

                String name, description, hours, startDate, endDate, courseType;            //Temporary variables to store the new course information entered by the user from the CLI
                System.out.println("Bitte neue Kursdaten angeben (Enter,falls keine Änderung gewünscht ist): "); // set the variables to the new values given by user
                System.out.println("Name: ");
                name = scan.nextLine();
                System.out.println("Beschreibung: ");
                description = scan.nextLine();
                System.out.println("Stundenanzahl: ");
                hours = scan.nextLine();
                System.out.println("Startdatum (YYYY-MM-DD): ");
                startDate = scan.nextLine();
                System.out.println("Enddatum (YYYY-MM-DD): ");
                endDate = scan.nextLine();
                System.out.println("Kurstyp (ZA/BF/FF/OE");
                courseType = scan.nextLine();

                //A new Optional course Object is created from the repository and updated using the update method
                Optional<Course> optionalCourseUpdated = repo.update(
                        new Course(                                                             // creates a new course
                                course.getID(),
                                name.equals("") ? course.getName() : name,                      // if the name is empty (user pressed Enter) then the old value is retained or else the name entered is used.
                                description.equals("") ? course.getDescription() : description,
                                hours.equals("") ? course.getHours() : Integer.parseInt(hours),
                                startDate.equals("") ? course.getBeginDate() : Date.valueOf(startDate),
                                endDate.equals("") ? course.getEndDate() : Date.valueOf(endDate),
                                courseType.equals("") ? course.getCourseType() : CourseType.valueOf(courseType)
                        ));
                optionalCourseUpdated.ifPresentOrElse(
                        (c) -> System.out.println("Kurs aktualisiert: " + c),                   // If the course was updated then this message is displayed
                        () -> System.out.println("Kurs konnte nicht aktualisiert werden!")      // If no course was updated then this message is displayed
                );
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
            System.out.println("Datenbankfehler bei Kurs-Detailanzeigen: " + databaseException.getMessage());
            // Handles exceptions thrown when the user SQL Database exceptions occurs. Returns an error message with the details of the exception.
        }
        catch(Exception exception){
            System.out.println("Unbekannter Fehler bei Kurs-Detailanzeigen: " + exception.getMessage());
            // Handles any other exceptions thrown. Returns an error message with the details of the exception.
        }
    }

    /**
     * Removes a course from the database
     * which corresponds to the ID number enter by the user
     */
    public void deleteCourse(){
        System.out.println("Welchen Kurs möchten Sie löschen? Bitte ID eingeben: ");
        Long courseToDeleteID = Long.parseLong(scan.nextLine());

        try {
            repo.deleteById(courseToDeleteID);
        }
        catch(IllegalArgumentException illegalArgumentException){
            // Handles exceptions thrown when the user enters one or more incorrect or illegal arguments. Returns an error message with the details of the exception.
            System.out.println("Eingabefehler: " + illegalArgumentException.getMessage());
        }
        catch(DatabaseException databaseException){
            System.out.println("Datenbankfehler beim löschen: " + databaseException.getMessage());
            // Handles exceptions thrown when the user SQL Database exceptions occurs. Returns an error message with the details of the exception.
        }
        catch(Exception exception){
            System.out.println("Unbekannter Fehler beim löschen: " + exception.getMessage());
            // Handles any other exceptions thrown. Returns an error message with the details of the exception.
        }
    }

    /**
     * Returns an Arraylist of Courses based on the user search
     */
    private void courseSearch(){
        System.out.println("Geben Sie einen Suchbegriff an!");
        String searchText = scan.nextLine();
        List<Course>courseList;
        try{
            courseList = repo.findAllCoursesByNameOrDescription(searchText);
            for (Course course: courseList){
                System.out.println(course);
            }

        }catch(DatabaseException databaseException){
            System.out.println("Datenbankfehler beim Kurs Suche: " + databaseException.getMessage());
        }catch(Exception exception){
            System.out.println("Unbekannter Fehler beim Kurs Suche: " + exception.getMessage());
        }
    }

    /**
     * Returns an Arraylist of Courses based on the user search
     */
    private void courseSearchbyName(){
        System.out.println("Geben Sie der Kurs Name (oder ein Teil davon) ein");
        String searchText = scan.nextLine();
        List<Course>courseList;
        try{
            courseList = repo.findAllCoursesByName(searchText);
            for (Course course: courseList){
                System.out.println(course);
            }

        }catch(DatabaseException databaseException){
            System.out.println("Datenbankfehler beim Kurs Suche: " + databaseException.getMessage());
        }catch(Exception exception){
            System.out.println("Unbekannter Fehler beim Kurs Suche: " + exception.getMessage());
        }
    }

    /**
     * Returns an Arraylist of Courses based on the user search
     */
    private void courseSearchbyDesc(){
        System.out.println("Geben Sie der Kurs Description (oder ein Teil davon) ein");
        String searchText = scan.nextLine();
        List<Course>courseList;
        try{
            courseList = repo.findAllCoursesByDescription(searchText);
            for (Course course: courseList){
                System.out.println(course);
            }

        }catch(DatabaseException databaseException){
            System.out.println("Datenbankfehler beim Kurs Suche: " + databaseException.getMessage());
        }catch(Exception exception){
            System.out.println("Unbekannter Fehler beim Kurs Suche: " + exception.getMessage());
        }
    }


    /**
     * Returns an Arraylist of Courses that are currently running
     */
    private void runningCourses(){
        System.out.println("Aktuell laufende Kurse: ");
        List<Course>list;
        try{
            list = repo.findAllRunningCourses();  //calls the method from the MySqlCourseRepository to find all courses currently running and save them in list
            for (Course course : list){           // iterates the list to print each course
                System.out.println(course);       // to print each course
            }
        }
        catch(DatabaseException databaseException){
            System.out.println("Datenbankfehler beim Kurs Suche :" + databaseException);
        }
    }


    /**
     *
     */
    private void coursesByStart(){
        System.out.println("Geben Sie bitte die Start Datum (YYYY-MM-DD) ein:  ");
        String searchText = scan.nextLine();
        List<Course>list;
        try{
            list = repo.findAllCoursesByStartDate(Date.valueOf(searchText));  //(calls the method from the MySqlCourseRepository to )find all courses scheduled to start on a date given by the user and save them in list
            for (Course course : list){           // iterates the list to print each course
                System.out.println(course);       // to print each course
            }
        }
        catch(DatabaseException databaseException){
            System.out.println("Datenbankfehler beim Kurs Suche :" + databaseException);
        }
    }

    //STUDENTS

    private void addStudent() throws DatabaseException, IllegalArgumentException{
        String f_name, l_name;                               //Temporary Variables for each Object Property, used to store the information entered by the user
        Date birthdate;

        try{
            System.out.println("Bitte alle Kursdaten angeben:");
            System.out.println("Vorname: ");
            f_name = scan.nextLine();
            if(f_name.equals("")) throw new IllegalArgumentException("Eingabe darf nicht leer sein!"); //UI (Client-Side) Validation in order to ensure that the data entered is database consistent.
            System.out.println("Nachname: ");
            l_name = scan.nextLine();
            if(l_name.equals("")) throw new IllegalArgumentException("Eingabe darf nicht leer sein!");
            System.out.println("Birthdate (YYYY-MM-DD): ");
            birthdate = Date.valueOf(scan.nextLine());
            System.out.println("Enddatum (YYYY-MM-DD): ");

            Optional<Student> optionalCourse = repo2.insert(
                    new Student(f_name, l_name, birthdate)
            );
            if(optionalCourse.isPresent()){
                System.out.println("Student:in eingefügt: " + optionalCourse.get());
            }else{
                System.out.println("Student:in könnte nicht eingefügt werden");
            }


        }
        catch(IllegalArgumentException illegalArgumentException){
            // Handles exceptions thrown when the user enters one or more incorrect or illegal arguments. Returns an error message with the details of the exception.
            System.out.println("Eingabefehler: " + illegalArgumentException.getMessage());
        }
        catch(InvalidValueException invalidValueException){
            System.out.println("Student:indaten nicht korrekt eingegeben: " + invalidValueException.getMessage());
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
    private void showAllStudents() throws DatabaseException {
        List<Student> list = null;

        list = repo2.getAll();

        try {
            if (list.size() > 0) {
                for (Student student : list) {
                    System.out.println(student);
                }
            } else {
                System.out.println("Keine Stundent:innen vorhanden!");
            }
        }catch(DatabaseException e){
            System.out.println("Datenbankfehler bei Anzeige aller Student:innen: " + e.getMessage());
        }catch(Exception exception){
            System.out.println("Unbekannter Fehler ist bei anzeigen alle Student:innen: " + exception.getMessage());
        }
    }
    private void showStudentDetails() throws DatabaseException {
        System.out.println("Für welchen Student:in möchten Sie die Details anzeigen? Bitte geben sie den Student:in ID ein");
        Long studentId = Long.parseLong(scan.nextLine());
        try{
            Optional<Student>studentOptional = repo2.getById(studentId);
            if(studentOptional.isPresent()){
                System.out.println(studentOptional.get());
            }else{
                System.out.println("Student:in mit der ID " + studentId + " nicht gefunden");
            }

        }catch(DatabaseException databaseException){
            System.out.println("Datenbankfehler bei Student:in Detailanzeigen " + databaseException.getMessage());

        }catch(Exception exception){
            System.out.println("Unbekannter Fehler bei Student:in Detailanzeigen " + exception.getMessage());
        }
    }
    private void updateStudentDetails() throws DatabaseException {
        System.out.println("Für welchen Kurs möchten Sie die Kursdetails ändern?");
        Long studentId = Long.parseLong(scan.nextLine());
        try {
            Optional<Student> studentOptional = repo2.getById(studentId);                   //get the course by the ID
            if (studentOptional.isEmpty()) {                                             //check whether a course with the ID entered exists.
                System.out.println("Kurs mit der ID " + studentId + " nicht gefunden");   // if the course doesn't exist then the user is informed
            } else {
                Student student = studentOptional.get();
                System.out.println("Anderung für folgenden Student: ");
                System.out.println(student);

                String f_name, l_name, birthdate;          //Temporary variables to store the new course information entered by the user from the CLI
                System.out.println("Bitte neue Kursdaten angeben (Enter,falls keine Änderung gewünscht ist): "); // set the variables to the new values given by user
                System.out.println("Vorname: ");
                f_name = scan.nextLine();
                System.out.println("Nachname: ");
                l_name = scan.nextLine();
                System.out.println("Geburtstagsdatum (YYYY-MM-DD): ");
                birthdate = scan.nextLine();


                //A new Optional Student Object is created from the repository and updated using the update method
                Optional<Student> optionalCourseUpdated = repo2.update(
                        new Student(                                                             // creates a new course
                                student.getID(),
                                f_name.equals("") ? student.getFirstName() : f_name,                      // if the name is empty (user pressed Enter) then the old value is retained or else the name entered is used.
                                l_name.equals("") ? student.getLastName() : l_name,
                                birthdate.equals("") ? student.getBirthdate() : Date.valueOf(birthdate)
                        ));
                optionalCourseUpdated.ifPresentOrElse(
                        (c) -> System.out.println("Student:in Details aktualisiert: " + c),                   // If the course was updated then this message is displayed
                        () -> System.out.println("Student:in konnte nicht aktualisiert werden!")      // If no course was updated then this message is displayed
                );
            }
        }
        catch(IllegalArgumentException illegalArgumentException){
            // Handles exceptions thrown when the user enters one or more incorrect or illegal arguments. Returns an error message with the details of the exception.
            System.out.println("Eingabefehler: " + illegalArgumentException.getMessage());
        }
        catch(InvalidValueException invalidValueException){
            System.out.println("Student:indaten nicht korrekt eingegeben: " + invalidValueException.getMessage());
            // Handles exceptions thrown when the user enters one or more invalid value. Returns an error message with the details of the exception.
        }
        catch(DatabaseException databaseException){
            System.out.println("Datenbankfehler bei Student:in Detailanzeigen: " + databaseException.getMessage());
            // Handles exceptions thrown when the user SQL Database exceptions occurs. Returns an error message with the details of the exception.
        }
        catch(Exception exception){
            System.out.println("Unbekannter Fehler bei Student:in Detailanzeigen: " + exception.getMessage());
            // Handles any other exceptions thrown. Returns an error message with the details of the exception.
        }
    }
    public void deleteStudent(){
        System.out.println("Welcher Student:in möchten Sie löschen? Bitte der ID eingeben: ");
        Long studentToDeleteID = Long.parseLong(scan.nextLine());

        try {
            repo2.deleteById(studentToDeleteID);
        }
        catch(IllegalArgumentException illegalArgumentException){
            // Handles exceptions thrown when the user enters one or more incorrect or illegal arguments. Returns an error message with the details of the exception.
            System.out.println("Eingabefehler: " + illegalArgumentException.getMessage());
        }
        catch(DatabaseException databaseException){
            System.out.println("Datenbankfehler beim löschen: " + databaseException.getMessage());
            // Handles exceptions thrown when the user SQL Database exceptions occurs. Returns an error message with the details of the exception.
        }
        catch(Exception exception){
            System.out.println("Unbekannter Fehler beim löschen: " + exception.getMessage());
            // Handles any other exceptions thrown. Returns an error message with the details of the exception.
        }
    }
    private void studentSearch(){
        System.out.println("Geben Sie einen Namen ein!");
        String searchText = scan.nextLine();
        List<Student>studentList;
        try{
            studentList = repo2.findStudentByName(searchText);
            for (Student student: studentList){
                System.out.println(student);
            }

        }catch(DatabaseException databaseException){
            System.out.println("Datenbankfehler beim Student Suche: " + databaseException.getMessage());
        }catch(Exception exception){
            System.out.println("Unbekannter Fehler beim Student Suche: " + exception.getMessage());
        }
    }

    private void studentsByBirthdate(){
        System.out.println("Geben Sie bitte die Geburtsdatum (YYYY-MM-DD) ein:  ");
        String searchText = scan.nextLine();
        List<Student>list;
        try{
            list = repo2.findStudentByBirthdate(Date.valueOf(searchText));  //(calls the method from the MySqlCourseRepository to )find all courses scheduled to start on a date given by the user and save them in list
            for (Student student : list){           // iterates the list to print each course
                System.out.println(student);       // to print each course
            }
        }
        catch(DatabaseException databaseException){
            System.out.println("Datenbankfehler beim Student:in Suche :" + databaseException);
        }
    }

    private void studentsByBirthdateInRange(){
        System.out.println("Geben Sie bitte die Geburtsdatum (YYYY-MM-DD) ein:  ");
        String date1 = scan.nextLine();
        System.out.println("Geben Sie bitte die Geburtsdatum (YYYY-MM-DD) ein:  ");
        String date2 = scan.nextLine();
        List<Student>list;
        try{
            list = repo2.findStudentBornDuringPeriod(Date.valueOf(date1), Date.valueOf(date2));  //(calls the method from the MySqlCourseRepository to )find all courses scheduled to start on a date given by the user and save them in list
            for (Student student : list){           // iterates the list to print each course
                System.out.println(student);       // to print each course
            }
        }
        catch(DatabaseException databaseException){
            System.out.println("Datenbankfehler beim Student:in Suche :" + databaseException);
        }
    }

}
