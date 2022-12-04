package domain;

/**
 * This Class is used to create an ID Object for other Classes in the package.
 *
 * @author Sonja Lechner
 * @version 1.0 19.11.2022
 *
 */
public abstract class BaseEntity {

    public Long id;

    public BaseEntity(Long id) {
    }

    /**
     * Default BaseEntity Constructor.
     * @param id used to set the id.
     * @throws InvalidValueException Exception thrown in case the id value entered is incorrect based on the parameters set
     */
    public void BaseEntity(Long id) throws InvalidValueException {
        setID(id);      //call the setID method in the constructor in order to avoid reproducing the business logic of the code.
    }

    /**
     *
     * @return id - returns the value of the id.
     */
    public Long getID(){
        return this.id;
    }

    /**
     *
     * @param id - the value of the id.
     * @throws InvalidValueException returns an exception message in case an incorrect value is entered.
     */
    public void setID(Long id) throws InvalidValueException {
        if (id==null|| id >= 0) {       //allows the id to be set to a value of null (e.g. via MySQL Insert Statement when the id is set to auto increment and assigned by the database)
            this.id = id;               //or to a value greater than or equal to 0 (e.g. a specific id should be used)
        }else {
            throw new InvalidValueException("Kurs-ID müss größer gleich 0 sein!"); //Exception message if invalid ID value is entered
        }
    }

    /**
     * coverts the ID from an integer to a string
     * @return a String
     */
    @Override
    public String toString() {
        return "BaseEntity{" +
                "id=" + id +
                '}';
    }
}
