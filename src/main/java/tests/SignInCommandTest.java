package tests;

import abstraction.Command;
import database.MysqlHandler;
import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import commands.SignInCommand;
import utils.SHA512;

import java.util.HashMap;
import java.util.LinkedHashMap;

import static org.junit.Assert.*;

public class SignInCommandTest {

    private static MysqlHandler mysqlHandler;
    private static Command command;

    @Before
    public void setUp() throws Exception {
        mysqlHandler = new MysqlHandler();
        cleanUp();
    }

    private void cleanUp()
    {
        mysqlHandler.connect();
        User.deleteAll();
        mysqlHandler.disconnect();
    }

    @After
    public void tearDown() throws Exception {
        cleanUp();
    }

    @Test
    public void execute() {

        HashMap<String, String> args = new HashMap();
        LinkedHashMap<String, Object> response;

        String email = "ouda@gmail.com";
        String password = "dragonballZ";

        args.put("email", "mohamed@gmail.com");
        args.put("password", password);

        command = new SignInCommand(args);
        response = command.execute();
        assertEquals("Log in should have failed as user does not exist", (Boolean) response.get("success"), false);

        args.put("email", email);

        mysqlHandler.connect();
        mysqlHandler.createUser(email, SHA512.hash(password));
        mysqlHandler.disconnect();

        args.put("password", "dragonballSuper");
        command = new SignInCommand(args);
        response = command.execute();
        assertEquals("Log in should have failed as user password does not match", (Boolean) response.get("success"), false);

        args.put("password", password);
        command = new SignInCommand(args);
        response = command.execute();
        assertEquals("Log in should succeed as sign in command was done by a verified user", (Boolean) response.get("success"), true);

    }
}