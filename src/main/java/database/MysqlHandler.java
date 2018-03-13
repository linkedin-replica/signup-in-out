package database;

import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.ConfigReader;

import java.sql.*;
import java.util.Properties;

public class MysqlHandler implements DatabaseHandler {

    private static final Logger LOGGER = LogManager.getLogger(MysqlHandler.class.getName());
    private String driverName, url, username, password;
    private Connection mysqlConnection;
    /**
     * Initialize the attributes of the database from the config file
     */
    public MysqlHandler() {
        Properties config = ConfigReader.readConfig();
        driverName = config.getProperty("development.driver");
        url = config.getProperty("development.url");
        username = config.getProperty("development.username");
        password = config.getProperty("development.password");
        connect();
    }

    /**
     * Open the connection to the mysql db using config file's attributes
     */
    public void connect() {
        if(mysqlConnection == null) {
            try {
                mysqlConnection = DriverManager.getConnection(url, username, password);
            } catch (SQLException e) {
                LOGGER.fatal("Couldn't connect to Mysql db");
                e.printStackTrace();
            }
        }
    }

    /**
     * Close the connection
     */
    public void disconnect() {
        if(mysqlConnection != null) {
            try {
                mysqlConnection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Get the user with it's email
     * @param email user email
     * @return Model of the User or null if it is not found
     */
    public Object getUser(String email) {
        String query = "{CALL search_for_user(?)}";
        CallableStatement statement = null;
        try {

            statement = mysqlConnection.prepareCall(query);
            statement.setString(1, email);
            statement.executeQuery();
            ResultSet results = statement.getResultSet();
            if (results.next())
                return new User(""+ results.getInt("id"), results.getString("email"), results.getString("password"));

        } catch (SQLException e) {
            LOGGER.warn("User is not exist");
            e.printStackTrace();
        }

        return null;
    }

    public String createUser(Object tmp) {
        User user = (User) tmp;
        return createUser(user.getEmail(), user.getPassword());
    }

    /**
     * Get the user with id userId
     * @param id user primary key
     * @return Model of the User or null if it is not found
     */
    public User getUserWithId(String id) {
        String query = "{CALL get_user(?)}";
        CallableStatement statement = null;
        try {
            statement = mysqlConnection.prepareCall(query);
            statement.setString(1, id);
            statement.executeQuery();
            ResultSet results = statement.getResultSet();
            if (results.next())
                return new User(""+ results.getInt("id"), results.getString("email"), results.getString("password"));

        } catch (SQLException e) {
            LOGGER.warn("User is not exist");
            e.printStackTrace();
        }

        return null;
    }


    /**
     * Create a user in db if it isn't exist
     * @param email
     * @param password
     * @return user id
     */
    public String createUser(String email, String password) {
        String query = "{CALL Insert_User(?, ?)}";
        CallableStatement statement = null;
        try {

            statement = mysqlConnection.prepareCall(query);
            statement.setString(1, email);
            statement.setString(2, password);
            statement.executeQuery();

        } catch (SQLException e) {

        }finally {

            return "" + ((User) getUser(email)).getId();
        }
    }

    /**
     * Delete all users from db
     */
    public void deleteAll(){

        String query = "{CALL delete_users()}";
        CallableStatement statement = null;
        try {

            statement = mysqlConnection.prepareCall(query);
            statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getMysqlConnection() {
        return mysqlConnection;
    }


}
