package database;

import model.User;

public interface DatabaseHandler {

    /**
     * Initiate a connection with the database
     */
    void connect();


    /**
     * Close a connection with the database
     */
    void disconnect();

    /**
     * Get a user with his email
     */
    Object getUser(String id);

    /**
     * Create a new user
     */
    String createUser(Object user);

}