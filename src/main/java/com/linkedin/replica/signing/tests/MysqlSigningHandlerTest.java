package com.linkedin.replica.signing.tests;
import com.linkedin.replica.signing.config.Configuration;
import com.linkedin.replica.signing.database.DatabaseConnection;
import com.linkedin.replica.signing.database.handlers.impl.MysqlSigningHandler;
import com.linkedin.replica.signing.models.User;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

public class MysqlSigningHandlerTest {
    private static MysqlSigningHandler mysqlDatabaseHandler;
    static Configuration config;

    @BeforeClass
    public static void init() throws IOException, SQLException {
        String rootFolder = "src/main/resources/config/";
        Configuration.init(rootFolder + "app.config",
                rootFolder + "arango.config",
                rootFolder + "mysql.config",
                rootFolder + "command.config");

        DatabaseConnection.init();
        config = Configuration.getInstance();

        mysqlDatabaseHandler= new MysqlSigningHandler();
    }

    @Before
    public void beforeEach()  {
        deleteUsers();
    }


    public static User getUser(String email) {
        User user = null;
        try {
            CallableStatement statement = mysqlDatabaseHandler.getDbInstance().prepareCall("{CALL search_for_user(?)}");
            statement.setString(1, email);
            statement.executeQuery();
            ResultSet results = statement.getResultSet();
            if (results.next()) {
                user = new User();
                user.setId("" + results.getInt("id"));
                user.setEmail(results.getString("email"));
                user.setPassword(results.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            return user;
        }
    }

    public static String createUser(User user) {
        try {
            CallableStatement statement = mysqlDatabaseHandler.getDbInstance().prepareCall("{CALL Insert_User(?, ?)}");
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getPassword());
            statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            return getUser(user.getEmail()).getId();
        }
    }

    public static void deleteUsers() {
        try {
            CallableStatement statement = mysqlDatabaseHandler.getDbInstance().prepareCall("{CALL delete_users()}");
            statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteUser(String email) {
        try {
            CallableStatement statement = mysqlDatabaseHandler.getDbInstance().prepareCall("{CALL delete_user(?)}");
            statement.setString(1, email);
            statement.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateUser() {
        String email = "Esraa.Khaled@golokoz.com";
        String password = "SokarNbat";

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        String userId = mysqlDatabaseHandler.createUser(user);

        User storedUser = getUser(email);
        assertEquals(String.format("The user of email: %s should exist in Users table", email), storedUser.getEmail(), email);
        assertEquals(String.format("Expected both users have the same password", password), storedUser.getPassword(), password);

        String duplicateUserId = createUser(user);
        assertEquals("Expected that the duplicate has the same id of the old one without exception", duplicateUserId, userId);

    }

    @Test
    public void testGetUser() {

        String email = "Esraa.Khaled@golokoz.com";
        String password = "SokarNbat";

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        createUser(user);

        User newUser = mysqlDatabaseHandler.getUser(email);
        assertEquals(String.format("Expected both users have the same email", email), email, newUser.getEmail());
        assertEquals(String.format("Expected both users have the same password", password), password, newUser.getPassword());

    }


    @AfterClass
    public static void clean() throws SQLException {
        DatabaseConnection.getInstance().closeConnections();
    }
}
