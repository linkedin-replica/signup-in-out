package com.linkedin.replica.signing.tests.core;

import com.arangodb.ArangoCollection;
import com.arangodb.ArangoDatabase;
import com.linkedin.replica.signing.database.handlers.impl.ArangoSqlSigningHandler;
import com.linkedin.replica.signing.database.DatabaseConnection;
import com.linkedin.replica.signing.database.handlers.impl.MysqlSigningHandler;
import com.linkedin.replica.signing.models.User;
import com.linkedin.replica.signing.tests.TestsUtils;
import org.junit.*;
import com.linkedin.replica.signing.config.Configuration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

public class ArangoSqlSigningHandlerTest {

    private static ArangoSqlSigningHandler arangoSqlSigningHandler;
    private static MysqlSigningHandler mysqlDatabaseHandler;
    private static Connection mysqlDbInstance;
    private static ArangoDatabase arangoDb;
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
        mysqlDbInstance = DatabaseConnection.getInstance().getMysqlDriver();


        arangoSqlSigningHandler = new ArangoSqlSigningHandler();
        arangoDb = DatabaseConnection.getInstance().getArangoDriver().db(
                config.getArangoConfigProp("db.name")
        );
    }

    @Before
    public void initBeforeTest() {
        arangoDb.createCollection(
                config.getArangoConfigProp("collection.name")
        );
        TestsUtils.deleteUsersSQL(mysqlDbInstance);
    }


    @Test
    public void testCreateUser() throws SQLException{

        String email = "example.mail@gmail.com";
        String firstname = "User firstname";
        String lastname = "User lastname";
        String password = "Pass";

        ArangoCollection arangoCollection = arangoDb.collection(config.getArangoConfigProp("collection.name"));

        User userProfile = new User();

        userProfile.setEmail(email);
        userProfile.setPassword(password);
        userProfile.setFirstName(firstname);
        userProfile.setLastName(lastname);


        String userId = arangoSqlSigningHandler.createUser(userProfile);
        assertEquals(String.format("The user of email: %s should exist in users collection", email), arangoCollection.documentExists(userId), true);

        User insertedUser = TestsUtils.getUserArango(userId,  arangoCollection);
        assertEquals(String.format("Expected both users have the same email: %s", email), email, insertedUser.getEmail());
        assertEquals(String.format("Expected both users have the same firstname: %s", firstname), firstname, insertedUser.getFirstName());
        assertEquals(String.format("Expected both users have the same lastname: %s", lastname), lastname, insertedUser.getLastName());

        User storedUser = TestsUtils.getUserSQL(email, mysqlDbInstance);
        assertEquals(String.format("The user of email: %s should exist in Users table", email), email, storedUser.getEmail());
        assertEquals("Expected both users have the same password", password, storedUser.getPassword());

        TestsUtils.deleteUserArango(userId, arangoCollection);
        assertEquals("The user should be deleted",arangoCollection.documentExists(userId), false);
    }

    @Test
    public void testGetUser() throws SQLException {
        String email = "example.mail@gmail.com";
        String firstname = "User firstname";
        String lastname = "User lastname";
        String password = "Pass";

        User userProfile = new User();

        userProfile.setEmail(email);
        userProfile.setPassword(password);
        userProfile.setFirstName(firstname);
        userProfile.setLastName(lastname);

        TestsUtils.createUserSQL(userProfile, mysqlDbInstance);

        User newUser = arangoSqlSigningHandler.getUser(email);
        assertEquals("Expected both users have the same email", email, newUser.getEmail());
        assertEquals("Expected both users have the same password", password, newUser.getPassword());
    }

    @After
    public void cleanAfterTest() {
        arangoDb.collection(
                config.getArangoConfigProp("collection.name")
        ).drop();
    }

    @AfterClass
    public static void clean() throws SQLException {
        DatabaseConnection.getInstance().closeConnections();
    }
}
