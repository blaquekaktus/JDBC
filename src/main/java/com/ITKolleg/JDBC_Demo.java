package com.ITKolleg;

import java.sql.*;

public class JDBC_Demo {


    public static void main(String[] args) {
        System.out.println("JDBC Demo!\n");
        //INSERT INTO `student` (`id`, `name`, `email`) VALUES (NULL, 'Jason Lechner', 'jaslechner@tsn.at'), (NULL, 'Josan Lechner', 'joslechner@tsn.at');
        //CREATE TABLE `studenten`.`student` ( `id` INT NOT NULL AUTO_INCREMENT , `name` VARCHAR(200) NOT NULL , `email` VARCHAR(200) NOT NULL , PRIMARY KEY (`id`)) ENGINE = InnoDB;
        selectAllDemo();
        insertStudentDemo();
        selectAllDemo();

    }

    public static void  insertStudentDemo(){
        System.out.println("\nInsert Demo mit JDBC\n");
        String connectionURL = "jdbc:mysql://localhost:3306/jdbc_demo"; //
        String user = "root";
        String pwd = "";

        try (Connection conn = DriverManager.getConnection(connectionURL, user, pwd)) {
            System.out.println("\nVerbinding zur Datenbank hergestellt!\n");
            PreparedStatement preparedStatement = conn.prepareStatement(
                    //"INSERT  INTO `student` (`id`, `name`, `email`) VALUES (NULL,`"Susan-Jolie Lechner"`,`"sulechner@gmx.at"`)"
                    // SQL Insert statements sollen so nicht gemacht um SQL Injections zu vermeiden.

                    "INSERT  INTO `student` (`id`, `name`, `email`) VALUES (NULL, ?, ?)" // ? is a placeholder to be replaced later with actual values
            );
            try {
                preparedStatement.setString(1,"Susan-Jolie Lechner");     //sets the value for the name in der vorherige SQL INSERT statement.
                preparedStatement.setString(2,"sulechner@gmx.at"); //sets the value for the email in der vorherige SQL INSERT statement.
                int rowsAffected = preparedStatement.executeUpdate();
                System.out.println(rowsAffected + " Datensätze eingefügt.");
            }catch(SQLException ex){
                System.out.println("Folgende Fehler SQL INSERT Statement gefunden: " + ex.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Folgende Fehler beim Verbingsversuch ist aufgetreten: " + e.getMessage());
        }
    }

    public static void selectAllDemo() {
        System.out.println("\nSelect Demo mit JDBC\n");
        String sqlSelectAllPersons = "Select * FROM `student`";
        String connectionURL = "jdbc:mysql://localhost:3306/jdbc_demo"; //


        try (Connection conn = DriverManager.getConnection(connectionURL, "root", "")) {
            System.out.println("\nVerbinding zur Datenbank hergestellt!\n");
            PreparedStatement preparedStatement = conn.prepareStatement("Select * FROM `student`");
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
}
