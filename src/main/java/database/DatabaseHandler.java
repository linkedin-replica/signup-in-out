package database;


import java.util.List;

public interface DatabaseHandler {
    /**
     * Initiate a connection with the database
     */
    void connect();


    /**
     * Close a connection with the database
     */
    void disconnect();
}