import dataaccess.MySQLDatabaseConnection;
import dataaccess.MySqlCourseRepository;
import dataaccess.MySqlStudentRepository;
import ui.Cli;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {

        try{
            MySQLDatabaseConnection.getConn("jdbc:mysql://localhost:3306/kurssystem", "root", "");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Cli myCli = null;
        try {
            myCli = new Cli(new MySqlCourseRepository());
        } catch (SQLException e) {
            System.out.println("Datenbank Fehler: " + e.getMessage() + "\n SQL State: " + e.getSQLState());
        } catch (ClassNotFoundException e) {
            System.out.println("Datenbank Fehler: " + e.getMessage());
        }
        myCli.start();

        Cli myCli2 = null;
        try {
            myCli2 = new Cli(new MySqlStudentRepository());
        } catch (SQLException e) {
            System.out.println("Datenbank Fehler: " + e.getMessage() + "\n SQL State: " + e.getSQLState());
        } catch (ClassNotFoundException e) {
            System.out.println("Datenbank Fehler: " + e.getMessage());
        }
        myCli2.start();

    }




}
