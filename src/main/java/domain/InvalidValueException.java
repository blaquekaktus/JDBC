package domain;

/**
 * Throws an Exception when the incorrect value for a parameter is entered.
 *
 * @author Sonja Lechner
 * @version 1.0 - 19.11.2022
 *
 */

public class InvalidValueException extends RuntimeException{

    /**
     * An unchecked Runtime Exception which occurs when a user attempts to assign an invalid value to a parameter.
     * @param message the message returned to the user when the exception is thrown.
     *
     */
    public InvalidValueException(String message){
        super(message);
    }

}
