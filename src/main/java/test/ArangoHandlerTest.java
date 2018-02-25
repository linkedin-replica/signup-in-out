package test;

import com.arangodb.ArangoDatabase;
import com.arangodb.entity.DocumentCreateEntity;
import database.ArangoHandler;
import org.junit.*;

import java.util.Properties;

import static org.junit.Assert.assertEquals;

import static utils.ConfigReader.readConfig;
import model.UserProfile;

public class ArangoHandlerTest {

    private static ArangoHandler arangoHandler;
    private static ArangoDatabase arangoDb;
    static Properties config;

    @BeforeClass
    public static void init() {
        config = readConfig();
        arangoHandler = new ArangoHandler();
        arangoHandler.connect();
        arangoDb = arangoHandler.getDBConnection().getArangoDriver().db(
                config.getProperty("db.name")
        );
    }

    @Before
    public void initBeforeTest() {
        arangoDb.collection(
                config.getProperty("collection.users.name")
        ).drop();

        arangoDb.createCollection(
                config.getProperty("collection.users.name")
        );
    }

    @Test
    public void testCreateUser() {

        String email = "nabila.ahmed@gmail.com";

        UserProfile userProfile = UserProfile.Instantiate();

        userProfile.setEmail(email);
        userProfile.setFirstName("Nabila");
        userProfile.setLastName("Ahmed");


        String userId = arangoHandler.createUser(userProfile);
        assertEquals(String.format("The user of email: %s should exist in users collection", email), arangoDb.collection(config.getProperty("collection.users.name")).documentExists(userId), true);

        arangoDb.collection(config.getProperty("collection.users.name")).deleteDocument(userId);
        assertEquals("The user should be deleted",arangoDb.collection(config.getProperty("collection.users.name")).documentExists(userId), false);
    }

    @Test
    public void testGetUser() {
        String email = "nabila.ahmed@gmail.com";
        String firstname = "Nabila";
        String lastname = "Ahmed";

        UserProfile userProfile = UserProfile.Instantiate();

        userProfile.setId("1");
        userProfile.setEmail(email);
        userProfile.setFirstName(firstname);
        userProfile.setLastName(lastname);

        DocumentCreateEntity user = arangoDb.collection(config.getProperty("collection.users.name")).insertDocument(userProfile);

        UserProfile newUser = (UserProfile) arangoHandler.getUser(user.getKey());
        assertEquals(String.format("Expected both users have the same email", email), email, newUser.getEmail());
        assertEquals(String.format("Expected both users have the same firstname", firstname), firstname, newUser.getFirstName());
        assertEquals(String.format("Expected both users have the same lastname", lastname), lastname, newUser.getLastName());

        arangoDb.collection(config.getProperty("collection.users.name")).deleteDocument(user.getKey());
        assertEquals("The user should be deleted",arangoDb.collection(config.getProperty("collection.users.name")).documentExists(user.getKey()), false);
    }


    @AfterClass
    public static void clean(){

        arangoHandler.getDBConnection().disconnect();
    }
}
