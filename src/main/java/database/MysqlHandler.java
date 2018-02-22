package database;

import model.User;
import org.javalite.activejdbc.Base;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 *
 */
public class MysqlHandler implements DatabaseHandler {


    /**
     *
     */
    private Properties properties;

    /**
     * @throws IOException
     */
    public MysqlHandler() throws IOException {
        properties = new Properties();
        properties.load(new FileInputStream("config"));
    }

    public void connect() {
        Base.open(properties.getProperty("development.driver"), properties.getProperty("development.url"), properties.getProperty("development.username"), properties.getProperty("development.password"));
    }

    public void disconnect() {
        Base.close();
    }

    /**
     * Get the user of an email or null if it's not found
     * @param email of the user
     * @return User
     */
    public User getUser(String email) {
        User u = User.findFirst("email = ?", email);
        System.out.println(u);
        return u;
    }

    /**
     * Create user if it already exist
     * @param email
     * @param password
     */
    public void createUser(String email, String password) {
        User user = new User();
        user.set("email", email);
        user.set("password", password);
        user.saveIt();
    }
}
