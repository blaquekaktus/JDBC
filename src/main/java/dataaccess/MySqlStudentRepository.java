package dataaccess;

import domain.Student;
import util.Assert;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 *
 * @author Sonja Lechner
 * @version 19.11.2022
 *
 */

public class MySqlStudentRepository implements MyStudentRepository {

    private final Connection CONN;

    /**
     * Class Constructor
     * @throws SQLException SQL exception thrown
     * @throws ClassNotFoundException Runtime exception
     */
    public MySqlStudentRepository() throws SQLException, ClassNotFoundException{
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
     * inserts a new Student Object (entity) into the Database
     * The Student Entity Properties are mapped to the Fields of the Database
     *
     * @param entity Optional The Student Object if successfully added to the database or an empty Optional Object is the INSERT Statement was unsuccessful.
     * @return Student Object (optional)
     */
    @Override
    //CREATE
    public Optional<Student> insert(Student entity) {
        Assert.notNull(entity);                     //checks to ensure that the Course Object to be added is not null
        try{
            String sql = "INSERT INTO `students` (firstName, lastName, `birthdate`) VALUES (?,?,?) ";
            // The SQL INSERT statement (can be auto copied directly from the database to ensure that the syntax is correct).
            PreparedStatement preparedStatement = CONN.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            // (Statement.RETURN_GENERATED_KEYS) Returns the auto generated keys (IDs)

            preparedStatement.setString(1, entity.getFirstName());           // Relational mapping of the Object Properties to the database fields
            preparedStatement.setString(2, entity.getLastName());
            preparedStatement.setDate(3, entity.getBirthdate());


            int affectedRows = preparedStatement.executeUpdate();           // Returns how many new rows were added to the database

            if (affectedRows == 0) {                                          // In this case we know that the INSERT statement did not work
                return Optional.empty();
            } else {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys(); // The generated keys are stored in the ResultSet Object generatedKeys
                if (generatedKeys.next()) {
                   return this.getById(generatedKeys.getLong(1));// Returns the Objects with the Ids stored in the ResultSet
                } else {
                    return Optional.empty();// Returns and empty Optional if the ResultSet is empty (in this case the INSERT STATEMENT did not execute successfully!)
                }
            }
        } catch (SQLException sqlException) {                                   // To catch any SQL exceptions
            throw new DatabaseException(sqlException.getMessage());
        }
    }

    /**
     * Returns a Student Object
     * If the Student is found an Optional Student object is returned and
     * an empty Optional is returned if the Student object is not found in the database.
     *
     * @param id Long The ID of the Student
     * @return Optional<Student>|Optional<> Returns an Optional Student Object if the Student is found or an empty Optional is the Student object does not.
     */
    @Override
    //READ
    public Optional<Student> getById(Long id){
        Assert.notNull(id);
        if(countStudentsInDbWithId(id)==0){
            return Optional.empty();
        }else{
            try {
                String sql = "SELECT * FROM `students` WHERE `id` = ?";
                PreparedStatement preparedStatement = CONN.prepareStatement(sql);
                preparedStatement.setLong(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                resultSet.next();
                Student student = new Student(                                      // A new Student object is created for each result.
                        resultSet.getLong("student_id"),                  // For each result found the field (column label) is mapped to a Course object property
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        resultSet.getDate("birthdate")
                );
                return Optional.of(student);
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
    //COUNT
    private int countStudentsInDbWithId(Long id){
        try {
            String countSql = "SELECT COUNT(*) FROM `students` WHERE `student_id` = ?";  // SQL Statement to return the number of courses with the given ID
            PreparedStatement preparedStatementCount = CONN.prepareStatement(countSql);  // Precompiled SQL Statements
            preparedStatementCount.setLong(1, id);                           // Sets the id parameter in the statement
            ResultSet resultSetCount = preparedStatementCount.executeQuery();            // Executes the SQL query iun the prepared statement and returns the ResultSet Object generated by the query
            resultSetCount.next();                                                       // The next element in ResultSet
            return resultSetCount.getInt(1);                                  // Returns the number of element IDs found in the ResultSet
        }
        catch (SQLException sqlException){
            throw new DatabaseException(sqlException.getMessage());
        }

    }

    /**
     * Returns all the Student Objects stored in the database
     * @return courseList A list of all the students found in the database
     */
    //READ ALL
    @Override
    public List<Student> getAll() throws DatabaseException {
        // Entities (A Row) from Relational Database is mapped to a Student Object!
        String sql = "SELECT * FROM `students`";                                // SELECT ALL statement saved as String 'sql'.
        try {
            PreparedStatement preparedStatement = CONN.prepareStatement(sql);   // sends the SQL Statement to the database
            ResultSet resultSet = preparedStatement.executeQuery();             // Results of SQL Statement saved as a Result Set through execute query
            ArrayList<Student> studentList = new ArrayList<>();                 // A new Array list is created to store all the results of the SQL Query as Student OBJECTS
            while(resultSet.next()){                                            // ResultSet iterated here
                studentList.add (new Student(                                   // A new Student object is created for each result.
                        resultSet.getLong("id"),              // For each result found the field (column label) is mapped to a Course object property
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        resultSet.getDate("birthdate")
                ));
            }
            return studentList;
        }catch (SQLException e) {
            throw new DatabaseException("Database error occurred!");
        }
    }

    /**
     * Updates the details of a student entity in the database
     *
     * @param entity the Student Object whose details are to be changed
     * @return the updated Student Object
     */
    @Override
    public Optional<Student> update(Student entity) {
        Assert.notNull(entity); //Checks to ensure that the object is not null
        String sql = "UPDATE `student` SET `firstName` = ?, `lastName` = ?, `birthdate` = ? WHERE `students`.`id` = ?";
        if(countStudentsInDbWithId(entity.getID())==0) {
            return Optional.empty();
        }else{
            try{
                PreparedStatement preparedStatement = CONN.prepareStatement(sql);
                preparedStatement.setString(1, entity.getFirstName());
                preparedStatement.setString(2, entity.getLastName());
                preparedStatement.setDate(3, entity.getBirthdate());
                preparedStatement.setLong(4, entity.getID());

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
     * Deletes a Student from the database based on the ID entered
     * @param id ID of the student to be deleted
     */
    @Override
    public void deleteById(Long id) {
        Assert.notNull(id);
        String sql = "DELETE FROM `students` WHERE `id` = ?";        // Precompiled SQL Statement to delete the database entity
        try{
            if(countStudentsInDbWithId(id)==1){
                PreparedStatement preparedStatement = CONN.prepareStatement(sql);
                preparedStatement.setLong(1, id);
                preparedStatement.executeUpdate();
            }
        }catch(SQLException sqlException){
            throw new DatabaseException(sqlException.getMessage());
        }
    }

    /**
     *
     * @param name
     * @return
     */
    public List<Student> findStudentByName(String name){
        try{
            String sql = "SELECT * FROM `students` LOWER(`first_name`) LIKE LOWER(?) OR LOWER(`last_name`) LIKE LOWER(?)";
            // Precompiled Select Statement which compares (LIKE) the lowercase equivalent (LOWER) of the first or lastname to the lowercase equivalent (LOWER(?)) of the search text (?)
            PreparedStatement preparedStatement = CONN.prepareStatement(sql);
            preparedStatement.setString(1,"%"+name+"%");   // search text wrapped in wild cards
            preparedStatement.setString(2,"%"+name+"%");
            ResultSet resultSet = preparedStatement.executeQuery();              // The results of the SQL Query is saved as resultSet
            ArrayList<Student> studentArrayList = new ArrayList<>();             // An ArrayList is created to store the later created Student  Objects
            while(resultSet.next()){                                             // ResultSet iterated here
                studentArrayList.add(new Student(                                // A new Student  object is created for each result.
                        resultSet.getLong("student_id"),               // For each result found the field (column label) is mapped to a Student object property
                        resultSet.getString("first_name"),
                        resultSet.getString("first_name"),
                        resultSet.getDate("birthdate")
                ));
            }
            return studentArrayList;                                             // The List of Students stored in the ArrayList
        }catch(SQLException sqlException){
            throw new DatabaseException(sqlException.getMessage());
        }
    }

    /**
     * Returns all the students found in the database based on the search entered
     * @param date the start date of the student to be found
     * @return Student Object The students found in the database based on the Date entered
     */
    @Override
    public List<Student> findStudentByBirthdate(Date date) {
        try {
            String sql = ("SELECT * FROM `students` WHERE `birthdate` = ?");   // Checks that the start date of the Student  matches the search date given
            PreparedStatement preparedStatement = CONN.prepareStatement(sql);
            preparedStatement.setDate(1,date);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Student> studentArrayList = new ArrayList<>();
            while (resultSet.next()) {
                studentArrayList.add(new Student(                                // A new Student  object is created for each result.
                        resultSet.getLong("student_id"),                     // For each result found the field (column label) is mapped to a Student  object property
                        resultSet.getString("first_name"),
                        resultSet.getString("first_name"),
                        resultSet.getDate("birthdate")
                ));
            }
            return studentArrayList;
        }
        catch(SQLException sqlException){
            throw new DatabaseException("Datenbankfehler beim Student Suche: " + sqlException.getMessage());
        }
    }

    /**
     * @param date1
     * @param date2
     * @return
     */
    @Override
    public List<Student> findStudentBornDuringPeriod(Date date1, Date date2) {
        try {
            String sql = ("SELECT * FROM `students` WHERE `birthdate` between ? and ?");   // Checks that the start date of the Student  matches the search date given
            PreparedStatement preparedStatement = CONN.prepareStatement(sql);
            if (date1.compareTo(date2)<0){
                preparedStatement.setDate(1, date1);
                preparedStatement.setDate(2, date2);
            } else {
                preparedStatement.setDate(1, date2);
                preparedStatement.setDate(1, date1);
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Student> studentArrayList = new ArrayList<>();
            while (resultSet.next()) {
                studentArrayList.add(new Student(                                // A new Student  object is created for each result.
                        resultSet.getLong("student_id"),                     // For each result found the field (column label) is mapped to a Student  object property
                        resultSet.getString("first_name"),
                        resultSet.getString("first_name"),
                        resultSet.getDate("birthdate")
                ));
            }
            return studentArrayList;
        }
        catch(SQLException sqlException){
            throw new DatabaseException("Datenbankfehler beim Student Suche: " + sqlException.getMessage());
        }
    }
}
