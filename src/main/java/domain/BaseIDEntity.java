package domain;

/**
 * This Class is used to create an ID Object for other Classes in the package.
 *
 * @author Sonja Lechner
 * @version 1.0 19.11.2022
 *
 */
public abstract class BaseIDEntity {

    public Long id;

    /**
     * Default BaseIDEntity Constructor.
     * @param id used to set the id.
     * @throws InvalidValueException Exception thrown in case the id value entered is incorrect based on the parameters set
     */
    public BaseIDEntity(Long id) throws InvalidValueException {
        setID(id);
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
        if (id==null|| id >= 0) {       //allows the id to be set to a value of null (eg. id is set to auto increment and assigned by the database)
            this.id = id;               //or to a value greater than or equal to 0 (eg. a specific id should be used)
        }else {
            throw new InvalidValueException("Kurs-ID müss größer gleich 0 sein!"); //Exception message if invalid ID value is entered
        }
    }

}
