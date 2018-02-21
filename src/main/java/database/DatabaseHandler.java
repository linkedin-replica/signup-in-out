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
    User getUser(String email);

    /**
     * Create a new user
     */
    void createUser(String email, String password);
}