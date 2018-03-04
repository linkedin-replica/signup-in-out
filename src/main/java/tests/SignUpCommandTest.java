package tests;

import abstraction.Command;
import com.arangodb.ArangoDatabase;
import database.ArangoHandler;
import database.MysqlHandler;
import model.User;
import model.UserProfile;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import commands.SignUpCommand;
import utils.SHA512;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Properties;

import static org.junit.Assert.*;
import static utils.ConfigReader.readConfig;

public class SignUpCommandTest {
    private static MysqlHandler mysqlHandler;
    private static Command command;
    private static ArangoHandler arangoHandler;
    private static ArangoDatabase arangoDb;
    static Properties config;

    @Before
    public void setUp() throws Exception {
        mysqlHandler = new MysqlHandler();
        config = readConfig();
        arangoHandler = new ArangoHandler();
        arangoHandler.connect();
        arangoDb = arangoHandler.getDBConnection().getArangoDriver().db(
                config.getProperty("db.name")
        );
        cleanUp();
    }

    private void cleanUp()
    {
        mysqlHandler.connect();
        User.deleteAll();
    }

    @After
    public void tearDown() throws Exception {
        mysqlHandler.disconnect();
        arangoHandler.getDBConnection().disconnect();

    }

    @Test
    public void execute() {
        String email = "Esraa.Khaled@golokoz.com";
        String password = "SokarNbat";
        String firstName = "walaa";
        String lastName = "bahaa";
        User user = new User();
        user.set("email", email);
        user.set("password", password);
        user.saveIt();

        HashMap<String, String> args = new HashMap();
        LinkedHashMap<String, Object> response;

        args.put("email", email);
        args.put("password", password);

        command = new SignUpCommand(args);
        response = command.execute();
        assertEquals("Sign up should have failed as missing information", (Boolean) response.get("success"), false);
        assertEquals("Error message indicants that user missing information", response.get("errMsg"), "Missing information");

        args.put("firstName", firstName);
        args.put("lastName", lastName);

        command = new SignUpCommand(args);
        response = command.execute();
        assertEquals("Sign up should have failed as user already exist", (Boolean) response.get("success"), false);
        assertEquals("Error message indicants that user already exist should returned", response.get("errMsg"), "This user already exists, Do you want to sign in?");
        email = "edsdsdsf";
        args.put("email", email); //Invalid email

        command = new SignUpCommand(args);
        response = command.execute();
        assertEquals("Sign up should have failed because of invalid email", (Boolean) response.get("success"), false);
        assertEquals("Error message indicants that the used email is invalid", response.get("errMsg"), "Invalid Email");
        email = "ahmed@gmail.com";
        args.put("email", email);
        command = new SignUpCommand(args);
        response = command.execute();
        assertEquals("Sign up should succeed", (Boolean) response.get("success"), true);

        assertEquals("Sign up should succeed", (Boolean) response.get("success"), true);

        UserProfile newUser = (UserProfile) arangoHandler.getUser((String) response.get("userId"));
        assertEquals(String.format("Expected both users have the same email: %s", email), email, newUser.getEmail());
        assertEquals(String.format("Expected both users have the same firstname: %s", firstName), firstName, newUser.getFirstName());
        assertEquals(String.format("Expected both users have the same lastname: %s", lastName), lastName, newUser.getLastName());

        mysqlHandler.connect();
        User newUserSql = (User) mysqlHandler.getUserWithId((String) response.get("userId"));
        assertEquals(String.format("Expected both users have the same email: %s", email), email, newUserSql.get("email"));
        assertEquals(String.format("Expected both users have the same password: %s", password), SHA512.hash(password), newUserSql.get("password"));




    }

}
