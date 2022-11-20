package dataaccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Manages the connection to a MySQL database
 *
 * @author Sonja Lechner
 * @version 1.0 - 19.11.2022
 */

public class MySQLDatabaseConnection {
    private static Connection conn = null;

    /**
     * Class constructor
     */
    private MySQLDatabaseConnection(){

    }

    /**
     *
     * @param url The URL for the connection
     * @param user The Username
     * @param pwd The Password (usually empty by default in MySQL)
     * @return returns the established connection
     * @throws ClassNotFoundException a checked exception which occurs when the specified class cannot be found
     * @throws SQLException an exception that provides information on a database access error
     * @see ClassNotFoundException
     * @see SQLException
     */

    public static Connection getConn(String url, String user, String pwd) throws ClassNotFoundException, SQLException {
        if(conn!=null){                                          //in the case that the connection already exists (not null)
            return conn;
        }else{
            Class.forName("com.mysql.cj.jdbc.Driver"); //throws the ClassNotFoundException if the class is not found
            conn = DriverManager.getConnection(url, user, pwd); //the established connection, if the connection is not possible the SQLException is thrown
        }
        return conn;
    }
}
