package com.ITKolleg;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBC_Demo {


    public static void main(String[] args) {
        System.out.println("JDBC Demo!\n");
        //INSERT INTO `student` (`id`, `name`, `email`) VALUES (NULL, 'Jason Lechner', 'jaslechner@tsn.at'), (NULL, 'Josan Lechner', 'joslechner@tsn.at');
        //CREATE TABLE `studenten`.`student` ( `id` INT NOT NULL AUTO_INCREMENT , `name` VARCHAR(200) NOT NULL , `email` VARCHAR(200) NOT NULL , PRIMARY KEY (`id`)) ENGINE = InnoDB;
   selectAllDemo();
    }

    public static void selectAllDemo() {
        System.out.println("Select Demo mit JDBC\n");
        String sqlSelectAllPersons = "Select * FROM `student`";
        String connectionURL = "jdbc:mysql://localhost:3306/jdbc_demo"; //
        try (Connection conn = DriverManager.getConnection(connectionURL, "root", "")) {
            System.out.println("Verbinding zur Datenbank hergestellt!");
        } catch (SQLException e) {
            System.out.println("Folgende Fehler beim Verbingsversuch ist aufgetreten: " + e.getMessage());
        }

    }
}
