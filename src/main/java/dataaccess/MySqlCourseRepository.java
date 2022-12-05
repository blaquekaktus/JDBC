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
 * @version 19.11.2022
 *
 */

public class MySqlCourseRepository implements MyCourseRepository {

    private final Connection CONN;

    /**
     * Class Constructor
     * @throws SQLException SQL exception thrown
     * @throws ClassNotFoundException Runtime exception
     */
    public MySqlCourseRepository() throws SQLException, ClassNotFoundException{
        //der Verbindung mit der Datenbank wird aufgebaut
        try {
            this.CONN = MySQLDatabaseConnection.getConn("jdbc:mysql://localhost:3306/kurssystem", "root", ""); //establishes a connection to the database
            System.out.println("Verbindung wird aufgebaut");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException ex) {
            throw new SQLException(ex);
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
            // The SQL INSERT statement (can be auto copied directly from the database to ensure that the syntax is correct).
            PreparedStatement preparedStatement = CONN.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
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
     * Returns a Course Object
     * If the Course is found an Optional Course object is returned and
     * an empty Optional is returned if the Course object is not found in the database.
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
                PreparedStatement preparedStatement = CONN.prepareStatement(sql);
                preparedStatement.setLong(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                resultSet.next();
                Course course = new Course(                                      // A new Course object is created for each result.
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

    /**
     * Returns the number of courses found in the database with the given ID
     *
     * @param id the id of the course to be searched for
     * @return courseCount the number of courses in the database with the given ID as an integer.
     */
    private int countCoursesInDbWithId(Long id){
        try {
            String countSql = "SELECT COUNT(*) FROM `courses` WHERE `id` = ?";         // SQL Statement to return the number of courses with the given ID
            PreparedStatement preparedStatementCount = CONN.prepareStatement(countSql);// Precompiled SQL Statements
            preparedStatementCount.setLong(1, id);                         // Sets the id parameter in the statement
            ResultSet resultSetCount = preparedStatementCount.executeQuery();          // Executes the SQL query iun the prepared statement and returns the ResultSet Object generated by the query
            resultSetCount.next();                                                     // The next element in ResultSet
            return resultSetCount.getInt(1);                                 //                      // Returns the number of element IDs found in the ResultSet
        }
        catch (SQLException sqlException){
            throw new DatabaseException(sqlException.getMessage());
        }

    }

    /**
     * Returns all the Course Object stored in the database
     * @return courseList A list of all the courses found in the database
     */
    @Override
    public List<Course> getAll() throws DatabaseException {
        // Entities (A Row) from Relational Database is mapped to a Course Object!
        String sql = "SELECT * FROM `courses`";                                 // SELECT ALL statement saved as String 'sql'.
        try {
            PreparedStatement preparedStatement = CONN.prepareStatement(sql);   // sends the SQL Statement to the database
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
     * Updates the details of a course entity in the database
     *
     * @param entity the Course Object whose details are to be changed
     * @return the updated Course Object
     */
    @Override
    public Optional<Course> update(Course entity) {

        Assert.notNull(entity); //Checks to ensure that the object is not null
        String sql = "UPDATE `courses` SET `name` = ?, `description` = ?, `hours` = ?, `begindate` = ?, `enddate` = ?, `coursetype` = ? WHERE `courses`.`id` = ?";
        if(countCoursesInDbWithId(entity.getID())==0) {
            return Optional.empty();
        }else{
            try{
                PreparedStatement preparedStatement = CONN.prepareStatement(sql);
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
     * Deletes a Course from the database based on the ID entered
     * @param id ID of the course to be deleted
     */
    @Override
    public void deleteById(Long id) {
        Assert.notNull(id);
        String sql = "DELETE FROM `courses` WHERE `id` = ?";        // Precompiled SQL Statement to delete the database entity
        try{
            if(countCoursesInDbWithId(id)==1){
                PreparedStatement preparedStatement = CONN.prepareStatement(sql);
                preparedStatement.setLong(1, id);
                preparedStatement.executeUpdate();
            }
        }catch(SQLException sqlException){
            throw new DatabaseException(sqlException.getMessage());
        }
    }

    /**
     * Returns the courses found in the database that match the search text entered
     * @param searchText the text to be compared to the name or description of the course to be found
     * @return Course Object The course found in the database based on the search entered
     */
    @Override
    public List<Course> findAllCoursesByNameOrDescription(String searchText) {
        try{
            String sql = "SELECT * FROM `courses` WHERE LOWER(`description)` LIKE LOWER(?) OR LOWER(`name`) LIKE LOWER(?) ";
            // Precompiled Select Statement which compares (LIKE) the lowercase equivalent (LOWER) of the name and description to the lowercase equivalent (LOWER(?)) of the search text (?)
            PreparedStatement preparedStatement = CONN.prepareStatement(sql);
            preparedStatement.setString(1,"%"+searchText+"%");   // search text wrapped in wild cards
            preparedStatement.setString(2,"%"+searchText+"%");
            ResultSet resultSet = preparedStatement.executeQuery();             // The results of the SQL Query is saved as resultSet
            ArrayList<Course> courseArrayList = new ArrayList<>();              // An ArrayList is created to store the later created Course Objects
            while(resultSet.next()){                                            // ResultSet iterated here
                courseArrayList.add (new Course(                                // A new Course object is created for each result.
                        resultSet.getLong("id"),                     // For each result found the field (column label) is mapped to a Course object property
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getInt("hours"),
                        resultSet.getDate("begindate"),
                        resultSet.getDate("enddate"),
                        CourseType.valueOf(resultSet.getString("coursetype"))
                ));
            }
            return courseArrayList;                                             // The List of Courses stored in the ArrayList
        }catch(SQLException sqlException){
            throw new DatabaseException(sqlException.getMessage());
        }
    }

    public List<Course> findAllCoursesByName(String searchText) {
        try{
            String sql = "SELECT * FROM `courses` LOWER(`name`) LIKE LOWER(?) ";
            // Precompiled Select Statement which compares (LIKE) the lowercase equivalent (LOWER) of the name to the lowercase equivalent (LOWER(?)) of the search text (?)
            PreparedStatement preparedStatement = CONN.prepareStatement(sql);
            preparedStatement.setString(1,"%"+searchText+"%");   // search text wrapped in wild cards
            ResultSet resultSet = preparedStatement.executeQuery();             // The results of the SQL Query is saved as resultSet
            ArrayList<Course> courseArrayList = new ArrayList<>();              // An ArrayList is created to store the later created Course Objects
            while(resultSet.next()){                                            // ResultSet iterated here
                courseArrayList.add (new Course(                                // A new Course object is created for each result.
                        resultSet.getLong("id"),                     // For each result found the field (column label) is mapped to a Course object property
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getInt("hours"),
                        resultSet.getDate("begindate"),
                        resultSet.getDate("enddate"),
                        CourseType.valueOf(resultSet.getString("coursetype"))
                ));
            }
            return courseArrayList;                                             // The List of Courses stored in the ArrayList
        }catch(SQLException sqlException){
            throw new DatabaseException(sqlException.getMessage());
        }
    }

    public List<Course> findAllCoursesByDescription(String searchText) {
        try{
            String sql = "SELECT * FROM `course` WHERE LOWER(`description)` LIKE LOWER(?) ";
            // Precompiled Select Statement which compares (LIKE) the lowercase equivalent (LOWER) of the description to the lowercase equivalent (LOWER(?)) of the search text (?)
            PreparedStatement preparedStatement = CONN.prepareStatement(sql);
            preparedStatement.setString(1,"%"+searchText+"%");   // search text wrapped in wild cards
            ResultSet resultSet = preparedStatement.executeQuery();             // The results of the SQL Query is saved as resultSet
            ArrayList<Course> courseArrayList = new ArrayList<>();              // An ArrayList is created to store the later created Course Objects
            while(resultSet.next()){                                            // ResultSet iterated here
                courseArrayList.add (new Course(                                // A new Course object is created for each result.
                        resultSet.getLong("id"),                     // For each result found the field (column label) is mapped to a Course object property
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getInt("hours"),
                        resultSet.getDate("begindate"),
                        resultSet.getDate("enddate"),
                        CourseType.valueOf(resultSet.getString("coursetype"))
                ));
            }
            return courseArrayList;                                             // The List of Courses stored in the ArrayList
        }catch(SQLException sqlException){
            throw new DatabaseException(sqlException.getMessage());
        }
    }

    /**
     * Returns all the active courses found in the database based on the search entered
     * @param courseType the course type of the course to be found
     * @return Course Object The courses found in the database based on the course type entered
     */
    @Override
    public List<Course> findAllCoursesByCourseType(CourseType courseType) {
        return null;
    }

    /**
     * Returns all the courses found in the database based on the search entered
     * @param startDate the start date of the course to be found
     * @return Course Object The courses found in the database based on the Date entered
     */
    @Override
    public List<Course> findAllCoursesByStartDate(Date startDate) {
        try {
            String sql = ("SELECT * FROM `courses` WHERE `begindate` = ?");   // Checks that the start date of the course matches the search date given
            PreparedStatement preparedStatement = CONN.prepareStatement(sql);
            preparedStatement.setDate(1,startDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Course> courseArrayList = new ArrayList<>();
            while (resultSet.next()) {
                courseArrayList.add(new Course(                                // A new Course object is created for each result.
                        resultSet.getLong("id"),                     // For each result found the field (column label) is mapped to a Course object property
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getInt("hours"),
                        resultSet.getDate("begindate"),
                        resultSet.getDate("enddate"),
                        CourseType.valueOf(resultSet.getString("coursetype"))
                ));
            }
            return courseArrayList;
        }
        catch(SQLException sqlException){
            throw new DatabaseException("Datenbankfehler beim Kurs Suche: " + sqlException.getMessage());
        }
    }


    /**
     * Returns all courses in the database which are currently running
     * @return courseArrayList An Array list of currently running courses
     */
    @Override
    public List<Course> findAllRunningCourses(){
        try{
            String sql = ("SELECT * FROM `courses` WHERE NOW()<`enddate`");                         // Checks that the end date of the course is later that today (NOW()<enddate)
            PreparedStatement preparedStatement = CONN.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Course>courseArrayList = new ArrayList<>();
            while (resultSet.next()){
                courseArrayList.add (new Course(                                // A new Course object is created for each result.
                        resultSet.getLong("id"),                     // For each result found the field (column label) is mapped to a Course object property
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getInt("hours"),
                        resultSet.getDate("begindate"),
                        resultSet.getDate("enddate"),
                        CourseType.valueOf(resultSet.getString("coursetype"))
                ));
            }
            return courseArrayList;
        }
        catch(SQLException sqlException){
            throw new DatabaseException("Datenbankfehler beim Kurs Suche: " + sqlException.getMessage());
        }
    }
}
