package com.linkedin.replica.signUpInOut.database.handlers;

public interface DatabaseHandler {

    /**
     * Initiate a connection with the com.linkedin.replica.signUpInOut.database
     */
    void connect();


    /**
     * Close a connection with the com.linkedin.replica.signUpInOut.database
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