package com.ITKolleg;

import java.sql.*;

public class JDBC_Demo {


    public static void main(String[] args) {
        System.out.println("JDBC Demo!");
        selectAllDemo();
        //insertStudentDemo();
        //insertStudentDirekt("Armin Lechner", "armin.lechner@gmx.at");
        //selectAllDemo();
        findAllByNameLike("on");
        //deleteStudentDemo(2);
        selectAllDemo();
        //updateStudentDemo();
        //selectAllDemo();


    }

    public static void selectAllDemo() {
        System.out.println("\nSelect Demo mit JDBC");
        String sqlSelectAllPersons = "Select * FROM `student`";
        String connectionURL = "jdbc:mysql://localhost:3306/jdbc_demo"; //
        String user = "root";
        String pwd = "";

        try (Connection conn = DriverManager.getConnection(connectionURL, user, pwd)) {
            System.out.println("Verbinding zur Datenbank hergestellt!\n");
            PreparedStatement preparedStatement = conn.prepareStatement(sqlSelectAllPersons);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                System.out.println("Student aus der Datenbank: ID " + id + ", Name: " + name + ", Email: " + email);
            }
        } catch (SQLException e) {
            System.out.println("Folgende Fehler beim Verbingsversuch ist aufgetreten: " + e.getMessage());
        }

    }

    public static void findAllByNameLike(String suche) {
        System.out.println("\nFind All By Name Like Demo mit JDBC");
        String connectionURL = "jdbc:mysql://localhost:3306/jdbc_demo"; //
        String user = "root";
        String pwd = "";
        String sqlSelectAllByNameLike = "Select * FROM `student` WHERE `student`.`name` LIKE ?";

        try (Connection conn = DriverManager.getConnection(connectionURL, user, pwd)) {
            System.out.println("Verbinding zur Datenbank hergestellt!\n");
            PreparedStatement preparedStatement = conn.prepareStatement(sqlSelectAllByNameLike);
            preparedStatement.setString(1, "%" + suche + "%");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                System.out.println("Student aus der Datenbank: ID " + id + ", Name: " + name + ", Email: " + email);
            }
        } catch (SQLException e) {
            System.out.println("Folgende Fehler beim Verbingsversuch ist aufgetreten: " + e.getMessage());
        }

    }

    public static void insertStudentDemo(){
        System.out.println("\nInsert Demo mit JDBC");
        String connectionURL = "jdbc:mysql://localhost:3306/jdbc_demo"; //
        String user = "root";
        String pwd = "";

        try (Connection conn = DriverManager.getConnection(connectionURL, user, pwd)) {
            System.out.println("Verbinding zur Datenbank hergestellt!\n");
            PreparedStatement preparedStatement = conn.prepareStatement(
                    //"INSERT  INTO `student` (`id`, `name`, `email`) VALUES (NULL,`"Susan-Jolie Lechner"`,`"sulechner@gmx.at"`)"
                    // SQL Insert statements sollen so nicht gemacht um SQL Injections zu vermeiden.

                    "INSERT  INTO `student` (`id`, `name`, `email`) VALUES (NULL, ?, ?)" // ? is a placeholder to be replaced later with actual values
            );
            try {
                preparedStatement.setString(1,"Penny Lechner");     //sets the value for the name in der vorherige SQL INSERT statement.
                preparedStatement.setString(2,"penlechner@tsn.at"); //sets the value for the email in der vorherige SQL INSERT statement.
                int rowsAffected = preparedStatement.executeUpdate();
                System.out.println(rowsAffected + " Datensätze eingefügt.");
            }catch(SQLException ex){
                System.out.println("Folgende Fehler SQL INSERT Statement gefunden: " + ex.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Folgende Fehler beim Verbingsversuch ist aufgetreten: " + e.getMessage());
        }
    }

    public static void insertStudentDirekt(String name, String email){
        System.out.println("\nInsert Demo mit JDBC");
        String connectionURL = "jdbc:mysql://localhost:3306/jdbc_demo"; //
        String user = "root";
        String pwd = "";

        try (Connection conn = DriverManager.getConnection(connectionURL, user, pwd)) {
            System.out.println("Verbinding zur Datenbank hergestellt!\n");
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "INSERT  INTO `student` (`id`, `name`, `email`) VALUES (NULL, ?, ?)" // ? is a placeholder to be replaced later with actual values
            );
            try {
                preparedStatement.setString(1,name);     //sets the value for the name in der vorherige SQL INSERT statement.
                preparedStatement.setString(2,email); //sets the value for the email in der vorherige SQL INSERT statement.
                int rowsAffected = preparedStatement.executeUpdate();
                System.out.println(rowsAffected + " Datensätze eingefügt.");
            }catch(SQLException ex){
                System.out.println("Folgende Fehler SQL INSERT Statement gefunden: " + ex.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Folgende Fehler beim Verbindunggsversuch ist aufgetreten: " + e.getMessage());
        }
    }

    public static void updateStudentDemo(){
        System.out.println("\nUpdate Demo mit JDBC");
        String connectionURL = "jdbc:mysql://localhost:3306/jdbc_demo"; //
        String user = "root";
        String pwd = "";
        int count = 0;

        try (Connection conn = DriverManager.getConnection(connectionURL, user, pwd)) {
            System.out.println("Verbinding zur Datenbank hergestellt!\n");
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "UPDATE `student` SET `name` = ? WHERE `student`.`id`= 3" // ? is a placeholder to be replaced later with actual values
            );
            PreparedStatement preparedStatement2 = conn.prepareStatement(
                    "UPDATE `student` SET `email` = ? WHERE `student`.`id`= 3" // ? is a placeholder to be replaced later with actual values
            );
            try {
                preparedStatement.setString(1,"Peter Bohringer");     //sets the value for the name in der vorherige SQL INSERT statement.
                preparedStatement2.setString(1,"p.bohringer@gmx.at");  //sets the value for the email in der vorherige SQL INSERT statement.

                preparedStatement.executeUpdate();
                count ++;
                preparedStatement2.executeUpdate();
                count ++;
                System.out.println(count + " aktualisierte Datensätze.");
            }catch(SQLException ex){
                System.out.println("Folgende Fehler SQL UPDATE Statement gefunden: " + ex.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Folgende Fehler beim Verbingsversuch ist aufgetreten: " + e.getMessage());
        }
    }

    public static void deleteStudentDemo(int studentID){
        System.out.println("\ndelete Demo mit JDBC");
        String connectionURL = "jdbc:mysql://localhost:3306/jdbc_demo"; //
        String user = "root";
        String pwd = "";

        try (Connection conn = DriverManager.getConnection(connectionURL, user, pwd)) {
            System.out.println("Verbinding zur Datenbank hergestellt!\n");
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "DELETE  FROM `student` WHERE `student`.`id`= ?" // ? is a placeholder to be replaced later with actual values
            );

            try {
                preparedStatement.setInt(1, studentID);     //sets the value for the ID in der vorherige SQL INSERT statement.

                int rowsAffected = preparedStatement.executeUpdate();

                System.out.println(rowsAffected + " Datensätze gelöscht.");
            }catch(SQLException ex){
                System.out.println("Folgende Fehler SQL DELETE Statement gefunden: " + ex.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Folgende Fehler beim Verbingsversuch ist aufgetreten: " + e.getMessage());
        }
    }


}
