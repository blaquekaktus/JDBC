package dataaccess;

import domain.Course;
import domain.CourseType;
import util.Assert;

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

    /**
     * Class Constructor
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public MySqlCourseRepository() throws SQLException, ClassNotFoundException{
        try {                   //der Verbindung mit der Datenbank wird aufgebaut
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
     *
     * @param id
     * @return
     */
    @Override
    public Optional<Course> getById(Long id){
        Assert.notNull(id);
        if(countCoursesInDbWithId(id)==0){
            return Optional.empty();
        }else{
            try {
                String sql = "SELECT * FROM `courses` WHERE `id` = ?";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setLong(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                resultSet.next();
                Course course = new Course(                                     // A new Course object is created for each result.
                        resultSet.getLong("id"),                      // For each result found the field (column label) is mapped to a Course object property
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getInt("hours"),
                        resultSet.getDate("begindate"),
                        resultSet.getDate("enddate"),
                        CourseType.valueOf(resultSet.getString("coursetype"))
                );
                return Optional.of(course);
            }catch(SQLException sqlException){
                throw new MySqlDatabaseException(sqlException.getMessage());
            }
        }
    }

    private int countCoursesInDbWithId(Long id){
        try {
            String countSql = "SELECT COUNT(*) FROM `courses` WHERE `id` = ?";
            PreparedStatement preparedStatementCount = conn.prepareStatement(countSql);
            preparedStatementCount.setLong(1, id);
            ResultSet resultSetCount = preparedStatementCount.executeQuery();
            resultSetCount.next();
            int courseCount = resultSetCount.getInt(1);
            return courseCount;
        }
        catch (SQLException sqlException){
            throw new MySqlDatabaseException(sqlException.getMessage());
        }

    }

    /**
     * @return
     */
    @Override
    public List<Course> getAll() throws MySqlDatabaseException{
        // Hier werden Daten (Eine Zeile) aus der Relational Datenbank zum Objekt 'gemappt'!
        String sql = "SELECT * FROM `courses`";                                 // SELECT ALL statement saved as String 'sql'.
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);   // sends the SQL Statement to the database
            ResultSet resultSet = preparedStatement.executeQuery();             // Results of SQL Statement saved as a Result Set through execute query
            ArrayList<Course> courseList = new ArrayList<>();                   // A new Array list is created to store all the results of the SQL Query as Course OBJECTS
            while(resultSet.next()){                                            // ResultSet iterated here
                courseList.add (new Course(                                     // A new Course object is created for each result.
                        resultSet.getLong("id"),                      // For each result found the field (column label) is mapped to a Course object property
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getInt("hours"),
                        resultSet.getDate("begindate"),
                        resultSet.getDate("enddate"),
                        CourseType.valueOf(resultSet.getString("coursetype"))
                ));
            }
            return courseList;
        } catch (SQLException e) {
            throw new MySqlDatabaseException("Database error occurred!");
        }
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
