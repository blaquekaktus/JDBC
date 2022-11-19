package dataaccess;

import java.util.List;
import java.util.Optional;

/**
 * Base Repository Interface
 *that has the database manipulation methods: CREATE, READ, UPDATE and DELETE
 *
 * @author Sonja Lechner
 * @version 1.0 19.11.2022
 */

public interface BaseRepository <T,I>{
    // Optional means that the return may be empty or contain

    Optional<T> insert (T entity);              // Create  - Insert Method
    Optional<T> getById (I id);                 // Read - Returns data based on a search by ID
    List<T> getAll();                           // Read - Returns a complete List of Entities
    Optional<T> update (T entity);              // Update - updates the Database und returns the updated data
    void deleteById(I id);                      // Delete - Delete a record based on the ID, returns nothing



}
