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
     * inserts a new Course Object (entity) into the Database
     * The Course Entity Properties are mapped to the Fields of the Database
     *
     * @param entity Optional The Course Object if successfully added to the database or an empty Optional Object is the INSERT Statement was unsuccessful.
     * @return Course Object (optional)
     */
    @Override
    public Optional<Course> insert(Course entity){
        Assert.notNull(entity);                     //checks to ensure that the Course Object to be added is not null
        try{
            String sql = "INSERT INTO `courses` (`name`, `description`, `hours`, `begindate`, `enddate`, `coursetype`) VALUES (?,?,?,?,?,?) ";
            // The SQL INSERT statement (can be auto copied directly from the database to ensure that the syntax is correct.
            PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            // (Statement.RETURN_GENERATED_KEYS) Returns the auto generated keys (IDs)
            preparedStatement.setString(1, entity.getName());           // Relational mapping of the Object Properties to the database fields
            preparedStatement.setString(2, entity.getDescription());
            preparedStatement.setInt(3, entity.getHours());
            preparedStatement.setDate(4, entity.getBeginDate());
            preparedStatement.setDate(5, entity.getEndDate());
            preparedStatement.setString(6, entity.getCourseType().toString()); //Course Type is saved as string in the database

            int affectedRows = preparedStatement.executeUpdate();           // Returns how many new rows were added to the database

            if(affectedRows == 0){                                          // In this case we know that the INSERT statement did not work
                return Optional.empty();
            }
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys(); // The generated keys are stored in the ResultSet Object generatedKeys

            if(generatedKeys.next()){
                return this.getById(generatedKeys.getLong(1));    // Returns the Objects with the Ids stored in the ResultSet
            }else{
                return Optional.empty();                                     // Returns and empty Optional if the ResultSet is empty (in this case the INSERT STATEMENT did not execute successfully!)
            }
        }catch(SQLException sqlException){                                   // To catch any SQL exceptions
            throw new DatabaseException(sqlException.getMessage());
        }
    }

    /**
     * Returns an Optional Object
     * If the Course is found an Optional Course object is returned and
     * an empty Optional is returned if the Course object is not found in the database.
     *
     *
     * @param id Long The ID of the Course
     * @return Optional<Course>|Optional<> Returns an Optional Course Object if the Course is found or an empty Optional is the Course object does not.
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
                throw new DatabaseException(sqlException.getMessage());
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
            throw new DatabaseException(sqlException.getMessage());
        }

    }

    /**
     * @return
     */
    @Override
    public List<Course> getAll() throws DatabaseException {
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
            throw new DatabaseException("Database error occurred!");
        }
    }

    /**
     * @param entity
     * @return
     */
    @Override
    public Optional<Course> update(Course entity) {

        Assert.notNull(entity);
        String sql = "UPDATE `courses` SET `name` = ?, `description` = ?, `hours` = ?, `begindate` = ?, `enddate` = ?, `coursetype` = ? WHERE `courses`.`id` = ?";
        if(countCoursesInDbWithId(entity.getID())==0) {
            return Optional.empty();
        }else{
            try{
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1, entity.getName());
                preparedStatement.setString(2, entity.getDescription());
                preparedStatement.setInt(3, entity.getHours());
                preparedStatement.setDate(4, entity.getBeginDate());
                preparedStatement.setDate(5, entity.getEndDate());
                preparedStatement.setString(6, entity.getCourseType().toString());
                preparedStatement.setLong(7, entity.getID());

                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows == 0){
                    return Optional.empty();
                }
                else{
                    return this.getById(entity.getID());
                }
            }catch (SQLException sqlException) {
                throw new DatabaseException(sqlException.getMessage());
            }
        }
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
