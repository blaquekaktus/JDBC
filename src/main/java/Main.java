import dataaccess.MySQLDatabaseConnection;
import dataaccess.MySqlCourseRepository;
import dataaccess.MySqlStudentRepository;
import ui.Cli2;


import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {

        try {
            MySQLDatabaseConnection.getConn("jdbc:mysql://localhost:3306/kurssystem", "root", "");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        Cli2 myCli = null;
        try {
            myCli = new Cli2(new MySqlCourseRepository(), new MySqlStudentRepository());
        } catch (SQLException e) {
            System.out.println("Datenbank Fehler: " + e.getMessage() + "\n SQL State: " + e.getSQLState());
        } catch (ClassNotFoundException e1) {
            System.out.println("Datenbank Fehler: " + e1.getMessage());
        }
        myCli.start();




    }




}
