package com.linkedin.replica.signing.core;

import com.linkedin.replica.signing.TestsUtils;
import com.linkedin.replica.signing.config.Configuration;
import com.linkedin.replica.signing.database.DatabaseConnection;
import com.linkedin.replica.signing.database.handlers.impl.MysqlSigningHandler;
import com.linkedin.replica.signing.models.User;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

public class MysqlSigningHandlerTest {
    private static MysqlSigningHandler mysqlDatabaseHandler;
    private static Connection dbInstance;
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
        dbInstance = DatabaseConnection.getInstance().getMysqlDriver();
        config = Configuration.getInstance();

        mysqlDatabaseHandler = new MysqlSigningHandler();
    }

    @Before
    public void beforeEach() {
        TestsUtils.deleteUsersSQL(dbInstance);
    }




    @Test
    public void testCreateUser() throws SQLException {
        String email = "example.mail@gmail.com";
        String password = "SokarNbat";

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        String userId = mysqlDatabaseHandler.createUser(user);

        User storedUser = TestsUtils.getUserSQL(email, dbInstance);
        assertEquals(String.format("The user of email: %s should exist in Users table", email), storedUser.getEmail(), email);
        assertEquals(String.format("Expected both users have the same password", password), storedUser.getPassword(), password);

    }

    @Test
    public void testCreateUserDuplicate() {
        String email = "example.mail@gmail.com";
        String password = "SokarNbat";

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        TestsUtils.createUserSQL(user, dbInstance);


        boolean duplicateException = false;
        try {
            mysqlDatabaseHandler.createUser(user);
        } catch (SQLException e) {
            duplicateException = true;
        }
        assertEquals("Expected a duplicate exception", duplicateException, true);

    }

    @Test
    public void testGetUser() throws SQLException {

        String email = "example.mail@gmail.com";
        String password = "SokarNbat";

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        TestsUtils.createUserSQL(user, dbInstance);

        User newUser = mysqlDatabaseHandler.getUser(email);
        assertEquals(String.format("Expected both users have the same email", email), email, newUser.getEmail());
        assertEquals(String.format("Expected both users have the same password", password), password, newUser.getPassword());

    }


    @AfterClass
    public static void clean() throws SQLException {
        DatabaseConnection.getInstance().closeConnections();
    }
}
