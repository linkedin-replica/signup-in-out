package tests;

import abstraction.Command;
import com.arangodb.ArangoDatabase;
import database.ArangoHandler;
import database.MysqlHandler;
import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import userCommands.SignInCommand;
import userCommands.SignUpCommand;
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
//        User.deleteAll();
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
//        User user = new User();
//        user.set("email", email);
//        user.set("password", password);
//        user.saveIt();

        HashMap<String, String> args = new HashMap();
        LinkedHashMap<String, Object> response;

        args.put("email", email);
        args.put("password", password);

        command = new SignUpCommand(args);
        response = command.execute();
        assertEquals("Sign up should have failed as missing information", (Boolean) response.get("success"), false);
        assertEquals("Error message indicants that user missing information", response.get("errMsg"), "Missing information");

        args.put("firstName", "walaa");
        args.put("lastName", "bahaa");

        command = new SignUpCommand(args);
        response = command.execute();
        assertEquals("Sign up should have failed as user already exist", (Boolean) response.get("success"), false);
        assertEquals("Error message indicants that user already exist should returned", response.get("errMsg"), "This user already exists, Do you want to sign in?");

        args.put("email", "edsdsdsf"); //Invalid email

        command = new SignUpCommand(args);
        response = command.execute();
        assertEquals("Sign up should have failed because of invalid email", (Boolean) response.get("success"), false);
        assertEquals("Error message indicants that the used email is invalid", response.get("errMsg"), "Invalid Email");

        args.put("email", "ahmed@gmail.com");
        command = new SignUpCommand(args);
        response = command.execute();
        assertEquals("Sign up should succeed", (Boolean) response.get("success"), true);
    }

}
