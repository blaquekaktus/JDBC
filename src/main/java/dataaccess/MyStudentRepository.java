package dataaccess;

import domain.Student;

import java.sql.Date;
import java.util.List;

/**
 *
 * Implements the DAO design pattern
 *
 * @author Sonja Lechner
 * @version 1.0 - 19.11.2022
 *
 */

public interface MyStudentRepository  extends BaseRepository<Student, Long>{

    List<Student> findStudentByName(String name);                       //returns a list of students to a search by name
    List<Student> findStudentByBirthdate(Date date);                    //returns a list of students to a search by birthdate
    List<Student> findStudentBornDuringPeriod(Date date1, Date date2);  //returns a list of students to a search by birthdate during a specific period

}
