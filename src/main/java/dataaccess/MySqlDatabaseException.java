package dataaccess;

public class MySqlDatabaseException extends RuntimeException {
    public MySqlDatabaseException(String message) {
        super(message);
    }
}
