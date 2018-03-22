package com.linkedin.replica.signing.tests.core;

import com.arangodb.ArangoDatabase;
import com.linkedin.replica.signing.config.Configuration;
import com.linkedin.replica.signing.database.DatabaseConnection;
import com.linkedin.replica.signing.exceptions.SigningException;
import com.linkedin.replica.signing.services.SigningService;
import com.linkedin.replica.signing.tests.TestsUtils;
import com.linkedin.replica.signing.utils.JwtUtils;
import org.junit.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class SigningServiceTest {
    private static SigningService signingService;
    private static ArangoDatabase arangoDb;
    private static Connection mysqlDBInstance;
    static Configuration config;

    @BeforeClass
    public static void init() throws IOException, SQLException {
        String rootFolder = "src/main/resources/config/";
        Configuration.init(rootFolder + "app.config",
                rootFolder + "arango.config",
                rootFolder + "mysql.config",
                rootFolder + "command.config",
                rootFolder + "controller.config");
        DatabaseConnection.init();
        config = Configuration.getInstance();
        signingService = new SigningService();
        mysqlDBInstance = DatabaseConnection.getInstance().getMysqlDriver();
        arangoDb = DatabaseConnection.getInstance().getArangoDriver().db(
                Configuration.getInstance().getArangoConfigProp("db.name")
        );
    }

    @Before
    public void initBeforeTest() throws IOException {
        arangoDb.createCollection(
                config.getArangoConfigProp("collection.name")
        );
    }

    @Test
    public void testService() throws Exception {

        // test signUp

        HashMap<String, String> args = new HashMap<>();
        args.put("email", "ahmed@gmail.com");
        args.put("password", "1234");
        args.put("firstName", "ahmed");
        args.put("lastName", "ouda");

        Boolean SignUpResponse = (Boolean) signingService.serve("signing.signUp", args);

        assertEquals("Created a new user" ,true, SignUpResponse);

        boolean duplicateUserRegistration = false;

        try {
            signingService.serve("signing.signUp", args);
        }
        catch (SigningException e) {
            duplicateUserRegistration = true;
        }

        assertEquals("Command should fail as user already exists" ,true, duplicateUserRegistration);

        // test signIn
        args.clear();
        args.put("email", "ahmed@gmail.com");
        args.put("password", "1234");

        String token = (String) signingService.serve("signing.signIn", args);

        assertEquals("Log in should succeed as sign in command was done by a verified user" ,true, JwtUtils.validateToken(token));

        args.clear();
        args.put("email", "ashraf@gmail.com");
        args.put("password", "1234");

        boolean wrongUsername = false;

        try {
            signingService.serve("signing.signIn", args);
        }
        catch (SigningException e) {
            wrongUsername = true;
        }

        assertEquals("Log in should have failed as user does not exist" ,true, wrongUsername);

        args.clear();
        args.put("email", "ahmed@gmail.com");
        args.put("password", "12345");

        boolean wrongPassword = false;

        try {
            signingService.serve("signing.signIn", args);
        }
        catch (SigningException e) {
            wrongPassword = true;
        }

        assertEquals("Log in should have failed as user does not exist" ,true, wrongPassword);
    }

    @After
    public void cleanAfterTest() throws IOException {
        arangoDb.collection(
                config.getArangoConfigProp("collection.name")
        ).drop();

        TestsUtils.deleteUsersSQL(mysqlDBInstance);
    }

    @AfterClass
    public static void clean() throws IOException, SQLException {
        DatabaseConnection.getInstance().closeConnections();
    }
}
