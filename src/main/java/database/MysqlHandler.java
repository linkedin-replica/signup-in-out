package database;

import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javalite.activejdbc.Base;
import utils.ConfigReader;

import java.util.Properties;

public class MysqlHandler implements DatabaseHandler {

    private static final Logger LOGGER = LogManager.getLogger(MysqlHandler.class.getName());
    private String driverName, url, username, password;

    /**
     * Initialize the attributes of the database from the config file
     */
    public MysqlHandler() {
        Properties config = ConfigReader.readConfig();
        driverName = config.getProperty("development.driver");
        url = config.getProperty("development.url");
        username = config.getProperty("development.username");
        password = config.getProperty("development.password");
    }

    /**
     * Open the connection to the mysql db using config file's attributes
     */
    public void connect() {
        Base.open(driverName, url, username, password);
    }

    /**
     * Close the connection
     */
    public void disconnect() {
        Base.close();
    }


    /**
     * Get the user with it's email
     * @param email user email
     * @return Model of the User or null if it is not found
     */
    public Object getUser(String email) {
        return User.findFirst("email = ?", email);
    }

    /**
     * Get the user with id userId
     * @param id user primary key
     * @return Model of the User or null if it is not found
     */
    public User getUserWithId(String id) {
        return User.findFirst("id=?", id);
    }


    /**
     * Create a user in db if it isn't exist
     * @param email
     * @param password
     * @return user id
     */
    public String createUser(String email, String password) {
        User user = new User();
        user.set("email", email);
        user.set("password", password);
        return this.createUser(user);
    }

    /**
     * Create a user in db if it isn't exist
     * @param tmpUser
     * @return Created user id in db
     */
    public String createUser(Object tmpUser) {
        User user = (User) tmpUser;
        try {
            user.saveIt();
        }catch (org.javalite.activejdbc.DBException e){
            LOGGER.warn(String.format("User of email: %s, already registered", user.getString("email")));
        }finally {
            return ((User)getUser(user.getString("email"))).getString("id");
        }
    }

    /**
     * Delete all users from db
     */
    public void deleteAll(){
        User.deleteAll();
    }
}
