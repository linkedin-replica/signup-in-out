package tests;

import abstraction.Command;
import database.MysqlHandler;
import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import userCommands.SignInCommand;
import userCommands.SignOutCommand;
import utils.JwtUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;

import static org.junit.Assert.*;

public class SignOutCommandTest {

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
    }

    @After
    public void tearDown() throws Exception {
        cleanUp();
    }

    @Test
    public void execute() {
        HashMap<String, String> args = new HashMap();
        HashMap<String, Object> claims = new HashMap<String, Object>();
        LinkedHashMap<String, Object> response;
        String jwtToken;

        jwtToken = (String)JwtUtils.generateToken(claims, "1", 0).get("token");
        command = new SignOutCommand(args);
        response = command.execute();
        assertEquals("Failed because of expired token", (Boolean) response.get("success"), false);


        jwtToken = (String)JwtUtils.generateToken(claims, "1", 2).get("token");
        args.put("jwtToken", jwtToken);
        command = new SignOutCommand(args);
        response = command.execute();
        assertEquals((String)response.get("errMsg"), (Boolean) response.get("success"), true);
    }
}