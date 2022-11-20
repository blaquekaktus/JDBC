import dataaccess.MySQLDatabaseConnection;
import dataaccess.MySqlCourseRepository;
import ui.Cli;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {

        Cli myCli = null;
        try {
            myCli = new Cli(new MySqlCourseRepository());
        } catch (SQLException e) {
            System.out.println("Datenbank Fehler: " + e.getMessage() + "\n SQL State: " + e.getSQLState());
        } catch (ClassNotFoundException e) {
            System.out.println("Datenbank Fehler: " + e.getMessage());
        }
        myCli.start();

        try {
            Connection myConnection =
                    MySQLDatabaseConnection.getConn("jdbc:mysql://localhost:3306/kurssystem", "root", "");
            System.out.println("\nVerbindung hergestellt");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




}
