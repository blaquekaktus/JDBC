import dataaccess.MySQLDatabaseConnection;
import ui.Cli;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {



        try {
            Connection myConnection =
                    MySQLDatabaseConnection.getConn("jdbc:mysql://localhost:3306/kurssystem", "root", "");
            System.out.println("\nVerbindung hergestellt");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Cli myCli = new Cli();
        myCli.start();

    }




}
