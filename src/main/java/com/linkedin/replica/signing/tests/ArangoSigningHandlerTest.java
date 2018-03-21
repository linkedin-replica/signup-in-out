package com.linkedin.replica.signing.tests;

import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDatabase;
import com.arangodb.entity.DocumentCreateEntity;
import com.linkedin.replica.signing.database.handlers.impl.ArangoSigningHandler;
import com.linkedin.replica.signing.database.DatabaseConnection;
import com.linkedin.replica.signing.models.User;
import org.junit.*;
import com.linkedin.replica.signing.config.Configuration;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ArangoSigningHandlerTest {

    private static ArangoSigningHandler arangoSigningHandler;
    private static ArangoDatabase arangoDb;
    static Configuration config;

    @BeforeClass
    public static void init() throws IOException, SQLException {
        String rootFolder = "src/main/resources/config/";
        Configuration.init(rootFolder + "app.config",
                rootFolder + "arango.config",
                rootFolder+ "mysql.config",
                rootFolder + "command.config");

        DatabaseConnection.init();
        config = Configuration.getInstance();
        arangoSigningHandler = new ArangoSigningHandler();
        arangoDb = DatabaseConnection.getInstance().getArangoDriver().db(
                Configuration.getInstance().getArangoConfig("db.name")
        );
    }

    @Before
    public void initBeforeTest() {
        arangoDb.createCollection(
                config.getArangoConfig("collection.name")
        );
    }

    static DocumentCreateEntity createUser(User user){
        return arangoDb.collection(config.getArangoConfig("collection.name")).insertDocument(user);
    }
    static void deleteUser(String userId){
        arangoDb.collection(config.getArangoConfig("collection.name")).deleteDocument(userId);
    }

    static User getUser(String userId) {
        return arangoDb.collection(config.getArangoConfig("collection.name")).getDocument(userId, User.class);
    }

    @Test
    public void testCreateUser() {

        String email = "example.mail@gmail.com";
        String firstname = "User firstname";
        String lastname = "User lastname";

        User userProfile = new User();

        userProfile.setEmail(email);
        userProfile.setFirstName(firstname);
        userProfile.setLastName(lastname);


        String userId = arangoSigningHandler.createUser(userProfile);
        assertEquals(String.format("The user of email: %s should exist in users collection", email), arangoDb.collection(config.getArangoConfig("collection.name")).documentExists(userId), true);

        User insertedUser = getUser(userId);
        assertEquals(String.format("Expected both users have the same email: %s", email), email, insertedUser.getEmail());
        assertEquals(String.format("Expected both users have the same firstname: %s", firstname), firstname, insertedUser.getFirstName());
        assertEquals(String.format("Expected both users have the same lastname: %s", lastname), lastname, insertedUser.getLastName());

        deleteUser(userId);
        assertEquals("The user should be deleted",arangoDb.collection(config.getArangoConfig("collection.name")).documentExists(userId), false);
    }

    @Test
    public void testGetUser() {
        String email = "example.mail@gmail.com";
        String firstname = "User firstname";
        String lastname = "User lastname";

        User userProfile = new User();


        userProfile.setEmail(email);
        userProfile.setFirstName(firstname);
        userProfile.setLastName(lastname);

        DocumentCreateEntity user = createUser(userProfile);

        User newUser = (User) arangoSigningHandler.getUser(user.getKey());
        assertEquals(String.format("Expected both users have the same email: %s", email), email, newUser.getEmail());
        assertEquals(String.format("Expected both users have the same firstname: %s", firstname), firstname, newUser.getFirstName());
        assertEquals(String.format("Expected both users have the same lastname: %s", lastname), lastname, newUser.getLastName());

        deleteUser(newUser.getId());
        assertEquals("The user should be deleted",arangoDb.collection(config.getArangoConfig("collection.name")).documentExists(user.getKey()), false);
    }

//
    @After
    public void cleanAfterTest() {
        arangoDb.collection(
                config.getArangoConfig("collection.name")
        ).drop();
    }

    @AfterClass
    public static void clean() throws SQLException {
        DatabaseConnection.getInstance().closeConnections();
    }
}
