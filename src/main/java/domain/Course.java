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
    public void setDescription(String description) {
        if((description != null) || (description.length()>1)) {       // sets a condition that the name may not be empty(null) or 1 character or less
            this.description = description;
        }else{                                                        // the following error message is displayed if the above condition is not met.
            throw new InvalidValueException("Kursdescription muss mindestens 2 Zeichen lang sein!");
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
     * Sets the value of the instance variable name
     * @param hours is used to set the value of the instance variable setHours
     * @throws InvalidValueException an error message is displayed in case the value entered does not meet the conditions set be this method
     */
    public void setHours(int hours) {
        if(hours != 0) {                                // sets the condition that the hours may not be 0
            this.hours = hours;
        }else{                                          // the following error message is displayed if the condition is not met
            throw new InvalidValueException("Kursname muss mindestens 2 Zeichen lang sein!");
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
    public void setBeginDate(Date beginDate) {
        if(beginDate != null) {                         // sets a condition that the date may not be empty(null) or 1 character or less
            this.beginDate = beginDate;
        }else{                                          // the following error message is displayed if the above condition is not met.
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
    public void setEndDate(Date endDate) {
        if(endDate != null){                            // sets a condition that the date may not be empty(null)
            this.endDate = endDate;
        }else{                                          // the following error message is displayed if the above condition is not met.
            throw new InvalidValueException("Kursname muss mindestens 2 Zeichen lang sein!");
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
     * @throws InvalidValueException an error message is displayed in case the value entered for name does not meet the conditions set be this method
     */
    public void setCourseType(CourseType courseType) {
        if(courseType != null) {       // sets a condition that the name may not be empty(null) or 1 character or less
            this.se = name;
        }else{                                          // the following error message is displayed
            throw new InvalidValueException("Kursname muss mindestens 2 Zeichen lang sein!");
        }
    }
}
