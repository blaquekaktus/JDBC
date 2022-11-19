package domain;

import java.sql.Date;

/**
 * This class creates Course objects
 *
 * @author Sonja Lechner
 * @version 1.0 - 19.11.2022
 *
 */

public class Course extends IDEntity{

    private String name;
    private String description;
    private int hours;
    private Date beginDate;
    private Date endDate;
    private CourseType courseType;

    /**
     * Default IDEntity Constructor.
     *
     * @param id used to set the id.
     * @throws InvalidValueException Exception thrown in case the id value entered is incorrect based on the parameters set
     */
    public Course(Long id) throws InvalidValueException {
        super(id);
    }

    /**
     * Returns the value of the instance variable name as a string
     * @return the name of the course as a string(name).
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the instance variable name
     * @param name The name of the course
     * @throws InvalidValueException an error message is displayed in case the value entered does not meet the conditions set by this method
     *
     */
    public void setName(String name) throws InvalidValueException{
        if((name != null) || (name.length()>1)) {       // sets the condition that the name may not be empty(null) or 1 character or less
            this.name = name;
        }else{                                          // the following error message is displayed if the above condition is not met.
            throw new InvalidValueException("Kursname muss mindestens 2 Zeichen lang sein!");
        }
    }

    /**
     * Returns the value of the instance variable description
     * @return the course description as a string
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the instance variable 'description'
     * @param description is used to set the value of the instance variable description
     * @throws InvalidValueException an error message is displayed in case the value entered does not meet the conditions set by this method
     */
    public void setDescription(String description) throws InvalidValueException{
        if((description != null) || (description.length()>10)) {       // ensures that the description name is not empty(null) or less than 1 character long
            this.description = description;
        }else{                                                        // displays an error message if the above condition is not met.
            throw new InvalidValueException("Kursdescription muss mindestens 10 Zeichen lang sein!");
        }
    }

    /**
     * Returns the value of the instance variable 'hours'
     * @return the length of the course in hours as an integer)
     */
    public int getHours() {
        return hours;
    }


    /**
     * Sets the value of the instance variable hours
     * @param hours is used to set the value of the instance variable setHours
     * @throws InvalidValueException an error message is displayed in case the value entered does not meet the conditions set by this method
     */
    public void setHours(int hours) throws InvalidValueException{
        if(hours > 0 && hours < 10) {                   // sets the condition that the hours must be between 1 and 10
            this.hours = hours;
        }else{                                          // the following error message is displayed if the condition is not met
            throw new InvalidValueException("Kurs Stunden darf nur zwischen 1 und 10 seinn!");
        }
    }

    /**
     * Returns the value of the instance variable beginDate
     * @return the start date of the course
     */
    public Date getBeginDate() {
        return beginDate;
    }

    /**
     * Sets the value of the instance variable beginDate
     * @param beginDate is used to set the value of the instance variable beginDate
     * @throws InvalidValueException an error message is displayed in case the value entered for name does not meet the conditions set be this method
     */
    public void setBeginDate(Date beginDate) throws InvalidValueException{
        if(beginDate != null) {                             //checks that the start date is set
            if(this.endDate != null) {                      //checks that the end date is already set
                if (beginDate.before(this.endDate)) {       //checks that the startdate is before the end date
                    this.beginDate = beginDate;
                } else {
                    throw new InvalidValueException("Kursbeginn muss VOR Kursende sein!"); //throws an exception if the start date is not before the end date
                }
            }else{
                this.beginDate = beginDate;
            }
        }else{
            throw new InvalidValueException("Startdatum darf nicht null/leer sein!");
        }
    }

    /**
     * returns the value of the instance variable endDate
     * @return the date the course ends
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * Sets the value of the instance variable endDate
     * @param endDate is used to assign the value of the instance variable endDate
     * @throws InvalidValueException an error message is displayed in case the value entered for name does not meet the conditions set be this method
     */
    public void setEndDate(Date endDate) throws InvalidValueException{
        if(endDate != null) {                              //checks that the end date is set
            if(this.beginDate != null) {                   //checks that the start date is already set
                if (endDate.after(this.beginDate)) {       //checks that the end date is after the start date
                    this.endDate = endDate;
                } else {
                    throw new InvalidValueException("Kursende muss NACH Kursbegin sein!"); //throws an exception if the end date is not after the start date
                }
            }else{
                this.beginDate = beginDate;
            }
        }else{
            throw new InvalidValueException("Enddatum darf nicht null/leer sein!");
        }
    }

    /**
     * Returns the value of the instance variable courseType
     * @return the course type
     */
    public CourseType getCourseType() {
        return courseType;
    }

    /**
     * Sets the value of the instance variable courseType
     * @param courseType is used to assign the value of the instance variable courseType
     * @throws InvalidValueException an error message is displayed in case the value entered does not meet the conditions set in this method
     */
    public void setCourseType(CourseType courseType) throws InvalidValueException{
        if(courseType != null) {                        // checks that the courde type is not empty(null)
            this.courseType = courseType;
        }else{                                          // the following error message is displayed
            throw new InvalidValueException("Kurstyp darf nicht null/leer sein!");
        }
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", hours=" + hours +
                ", beginDate=" + beginDate +
                ", endDate=" + endDate +
                ", courseType=" + courseType +
                '}';
    }

}

