package dataaccess;

import domain.Course;
import domain.CourseType;
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

public interface MyCourseRepository extends BaseRepository<Course, Long>{

    List<Course> findAllCoursesByName(String name);                             //returns a list of courses to a search by name
    List<Course> findAllCoursesByDescription(String description);               //returns a list of courses to a search by description
    List<Course> findAllCoursesByNameOrDescription(String searchText);          //returns a list of courses to a search by name or description
    List<Course> findAllCoursesByCourseType(CourseType courseType);             //returns a list of courses to a search by course type
    List<Course> findAllCoursesByStartDate(Date startDate);                     //returns a list of courses to a search by start date
    List<Course> findAllRunningCourses();                                        //returns a list of courses that are in progress
}
