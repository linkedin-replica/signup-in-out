package com.linkedin.replica.signing.tests;

import static org.junit.Assert.assertEquals;

public class ArangoSigningHandlerTest {
//
//    private static ArangoSigningHandler arangoDatabaseHandler;
//    private static ArangoDatabase arangoDb;
//    static Properties config;
//
//    @BeforeClass
//    public static void init() {
//        config = readDatabaseConfig();
//        arangoDatabaseHandler = new ArangoSigningHandler();
//        arangoDatabaseHandler.connect();
//        arangoDb = arangoDatabaseHandler.getDBConnection().getArangoDriver().db(
//                config.getProperty("db.name")
//        );
//    }
//
//    @Before
//    public void initBeforeTest() {
//        arangoDb.collection(
//                config.getProperty("collection.users.name")
//        ).drop();
//
//        arangoDb.createCollection(
//                config.getProperty("collection.users.name")
//        );
//    }
//
//    @Test
//    public void testCreateUser() {
//
//        String email = "nabila.ahmed@gmail.com";
//
//        UserProfile userProfile = UserProfile.Instantiate();
//
//        userProfile.setEmail(email);
//        userProfile.setFirstName("Nabila");
//        userProfile.setLastName("Ahmed");
//
//
//        String userId = arangoDatabaseHandler.createUser(userProfile);
//        assertEquals(String.format("The user of email: %s should exist in users collection", email), arangoDb.collection(config.getProperty("collection.users.name")).documentExists(userId), true);
//
//        arangoDb.collection(config.getProperty("collection.users.name")).deleteDocument(userId);
//        assertEquals("The user should be deleted",arangoDb.collection(config.getProperty("collection.users.name")).documentExists(userId), false);
//    }
//
//    @Test
//    public void testGetUser() {
//        String email = "nabila.ahmed@gmail.com";
//        String firstname = "Nabila";
//        String lastname = "Ahmed";
//        String key = "1";
//
//        UserProfile userProfile = UserProfile.Instantiate();
//
//        userProfile.setKey(key);
//        userProfile.setEmail(email);
//        userProfile.setFirstName(firstname);
//        userProfile.setLastName(lastname);
//
//        DocumentCreateEntity user = arangoDb.collection(config.getProperty("collection.users.name")).insertDocument(userProfile);
//
//        UserProfile newUser = (UserProfile) arangoDatabaseHandler.getUser(key);
//        assertEquals(String.format("Expected both users have the same email: %s", email), email, newUser.getEmail());
//        assertEquals(String.format("Expected both users have the same firstname: %s", firstname), firstname, newUser.getFirstName());
//        assertEquals(String.format("Expected both users have the same lastname: %s", lastname), lastname, newUser.getLastName());
//
//        arangoDb.collection(config.getProperty("collection.users.name")).deleteDocument(user.getKey());
//        assertEquals("The user should be deleted",arangoDb.collection(config.getProperty("collection.users.name")).documentExists(user.getKey()), false);
//    }
//
//
//    @AfterClass
//    public static void clean(){
//
//        arangoDatabaseHandler.getDBConnection().disconnect();
//    }
}
