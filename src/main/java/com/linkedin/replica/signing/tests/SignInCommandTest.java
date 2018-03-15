package com.linkedin.replica.signing.tests;

import com.linkedin.replica.signing.commands.Command;
import com.linkedin.replica.signing.database.handlers.impl.MysqlSigningHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.linkedin.replica.signing.commands.impl.SignInCommand;
import com.linkedin.replica.signing.utils.SHA512;

import java.util.HashMap;
import java.util.LinkedHashMap;

import static org.junit.Assert.*;

public class SignInCommandTest {

//    private static MysqlSigningHandler mysqlDatabaseHandler;
//    private static Command command;
//
//    @Before
//    public void setUp() throws Exception {
//        mysqlDatabaseHandler = new MysqlSigningHandler();
//        cleanUp();
//    }
//
//    private void cleanUp()
//    {
//        mysqlDatabaseHandler.connect();
//    }
//
//    @After
//    public void tearDown() throws Exception {
//        cleanUp();
//    }
//
//    @Test
//    public void execute() {
//
//        HashMap<String, String> args = new HashMap();
//        LinkedHashMap<String, Object> response;
//
//        String email = "ouda@gmail.com";
//        String password = "dragonballZ";
//
//        args.put("email", "mohamed@gmail.com");
//        args.put("password", password);
//
//        command = new SignInCommand(args);
//        response = command.execute();
//        assertEquals("Log in should have failed as user does not exist", (Boolean) response.get("success"), false);
//
//        args.put("email", email);
//
//        mysqlDatabaseHandler.connect();
//        mysqlDatabaseHandler.createUser(email, SHA512.hash(password));
//        mysqlDatabaseHandler.disconnect();
//
//        args.put("password", "dragonballSuper");
//        command = new SignInCommand(args);
//        response = command.execute();
//        assertEquals("Log in should have failed as user password does not match", (Boolean) response.get("success"), false);
//
//        args.put("password", password);
//        command = new SignInCommand(args);
//        response = command.execute();
//        assertEquals("Log in should succeed as sign in command was done by a verified user", (Boolean) response.get("success"), true);
//
//    }
}