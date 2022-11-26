package util;

/**
 * Checks that the given object in not null
 *
 * @author Sonja Lechner
 * @version 22.11.2022
 */

public class Assert {

    /**
     *
     * @param o Object the given object
     * @throws IllegalArgumentException if the given object is null
     */

    public static void notNull(Object o) throws IllegalArgumentException{               //checks that the object given is not null
        if(o==null) throw new IllegalArgumentException("Reference must not be null!");
    }
}
