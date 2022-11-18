package dataaccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLDatabaseConnection {
    private static Connection conn = null;

    private MySQLDatabaseConnection(){

    }

    public static Connection getConn(String url, String user, String pwd) throws ClassNotFoundException, SQLException {
        if(conn!=null){
            return conn;
        }else{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, pwd);
        }
        return conn;
    }
}
