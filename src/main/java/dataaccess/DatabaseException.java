package dataaccess;

/**
 * A database runtime exception
 * that returns a message when thrown.
 *
 * @author Sonja Lechner
 * @version 19.11.2022
 */
public class DatabaseException extends RuntimeException {
    public DatabaseException(String message) {
        super(message);
    }
}
