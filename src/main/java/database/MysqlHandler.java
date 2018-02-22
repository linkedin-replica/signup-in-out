package database;

import model.User;
import org.javalite.activejdbc.Base;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class MysqlHandler implements DatabaseHandler {


    private Properties properties;

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

    public User getUser(String email) {
        User u = User.findFirst("email = ?", email);
        System.out.println(u);
        return u;
    }

    public void createUser(String email, String password) {
        User u = new User();
        u.set("email", email);
        u.set("password", password);
        u.saveIt();
    }
}
