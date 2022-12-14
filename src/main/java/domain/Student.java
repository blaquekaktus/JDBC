package domain;
import java.sql.Date;

/**
 * This class creates Student objects
 *
 * @author Sonja Lechner
 * @version 1.0 - 19.11.2022
 *
 */

public class Student extends BaseEntity {


    private String firstName;
    private String lastName;
    private Date birthdate;


    /**
     * @param student_id the course ID as Long
     * @param firstName  the first name of the Student as a String
     * @param lastName   the last name of the Student as a String
     * @param birthdate  the date of birth of the Student as a Date
     */
    public Student(Long student_id, String firstName, String lastName, Date birthdate) throws InvalidValueException {
        super(student_id);
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setBirthdate(birthdate);

    }

    /**
     * @param firstName the first name of the Student as a String
     * @param lastName  the last name of the Student as a String
     * @param birthdate the date of birth of the Student as a Date
     */
    public Student(String firstName, String lastName, Date birthdate) throws InvalidValueException {
        super(null);
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setBirthdate(birthdate);

    }


    /**
     * Returns the value of the firstName as a string
     * @return the name of the course as a string.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param name The first name of the student
     * @throws InvalidValueException an error message is displayed in case the value entered does not meet the conditions set by this method
     */
    public void setFirstName(String name) throws InvalidValueException {
        if ((name != null) && (name.length() >=2)) {       // sets the condition that the name may not be empty(null) and must be atleast 2 characters long.
            this.firstName = name;
        } else {                                          // the following error message is displayed if the above condition is not met.
            throw new InvalidValueException("Der Vorname muss mindestens 2 Zeichen lang sein!");
        }
    }

    /**
     * Returns the value of the instance variable name as a string
     * @return the name of the course as a string(name).
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param name The last name of the student
     * @throws InvalidValueException an error message is displayed in case the name entered does not meet the conditions set by this method
     */
    public void setLastName(String name) throws InvalidValueException {
        if ((name != null) && (name.length() > 1)) {       // sets the condition that the name may not be empty(null) and must be more than 1 character long
            this.lastName = name;
        } else {                                          // the following error message is displayed if the above condition is not met.
            throw new InvalidValueException("Kursname muss mindestens 2 Zeichen lang sein!");
        }
    }


    /**
     * Returns the birthdate of the Student
     * @return the birthdate of the Student
     */
    public Date getBirthdate() {
        return birthdate;
    }

    /**
     * Sets the birthdate of the Student
     *
     * @param birthdate is used to set the value of the instance variable birthdate
     * @throws InvalidValueException an error message is displayed in case the value entered for name does not meet the conditions set be this method
     */
    public void setBirthdate(Date birthdate) throws InvalidValueException {
        if (birthdate != null) {
            this.birthdate = birthdate;
        } else {
            throw new InvalidValueException("Geburtstagdatum darfnicht leer sein!"); //throws an exception if the start date is not before the end date
        }
    }
}



