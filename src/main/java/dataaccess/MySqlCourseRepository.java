package dataaccess;

import domain.Course;
import domain.CourseType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * I
 *
 * @author Sonja Lechner
 * @version 1.0 - 19.11.2022
 *
 */

public class MySqlCourseRepository implements MyCourseRepository {

    private Connection conn;

    public MySqlCourseRepository() throws SQLException, ClassNotFoundException{
        try {
            this.conn = MySQLDatabaseConnection.getConn("jdbc:mysql://localhost:3306/kurssystem", "root", "");
            System.out.println("Verbindung wird aufgebaut");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param entity
     * @return 
     */
    @Override
    public Optional<Course> insert(Course entity) {
        return Optional.empty();
    }

    /**
     * @param id
     * @return
     */
    @Override
    public Optional<Course> getById(Long id) {
        return Optional.empty();
    }

    /**
     * @return
     */
    @Override
    public List<Course> getAll() {
        String sql = "Select * from `courses`";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Course> courseList = new ArrayList<>();
            while(resultSet.next()){
                courseList.add(new Course(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getInt("hours"),
                        resultSet.getDate("beginDate"),
                        resultSet.getDate("endDate"),
                        CourseType.valueOf(resultSet.getString("courseType"))
                ));

            }
        } catch (SQLException e) {
            throw new MySqlDatabaseException("Database error occured!");

        }
        return null;
    }

    /**
     * @param entity
     * @return
     */
    @Override
    public Optional<Course> update(Course entity) {
        return Optional.empty();
    }

    /**
     * @param id
     */
    @Override
    public void deleteById(Long id) {

    }

    /**
     * @param name
     * @return
     */
    @Override
    public List<Course> findAllCoursesByName(String name) {
        return null;
    }

    /**
     * @param description
     * @return
     */
    @Override
    public List<Course> findAllCoursesByDescription(String description) {
        return null;
    }

    /**
     * @param searchText
     * @return
     */
    @Override
    public List<Course> findAllCoursesByNameOrDescription(String searchText) {
        return null;
    }

    /**
     * @param courseType
     * @return
     */
    @Override
    public List<Course> findAllCoursesByCourseType(CourseType courseType) {
        return null;
    }

    /**
     * @param startDate
     * @return
     */
    @Override
    public List<Course> findAllCoursesByStartDate(Date startDate) {
        return null;
    }

    /**
     * @return
     */
    @Override
    public List<Course> findAllRunningCourses() {
        return null;
    }
}
