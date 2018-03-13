package com.linkedin.replica.signUpInOut.database.handlers.impl;

import com.arangodb.ArangoCollection;
import com.arangodb.ArangoDB;
import com.arangodb.ArangoDatabase;
import com.arangodb.entity.DocumentCreateEntity;
import com.linkedin.replica.signUpInOut.database.handlers.DatabaseHandler;
import com.linkedin.replica.signUpInOut.models.UserProfile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Properties;
import static com.linkedin.replica.signUpInOut.utils.ConfigReader.readConfig;

public class ArangoDatabaseHandler implements DatabaseHandler {

    private static final Logger LOGGER = LogManager.getLogger(ArangoDatabaseHandler.class.getName());

    private Properties configProps;
    private ArangoDB arangoDriver;
    private ArangoDatabase database;
    private ArangoCollection collection;
    private static ArangoDatabaseHandler dbConnection; //


    public ArangoDatabaseHandler(){
        configProps = readConfig();
        initializeArangoDB();
    }

    private void initializeArangoDB() {
        arangoDriver = new ArangoDB.Builder()
                .user(configProps.getProperty("arangodb.user"))
                .password(configProps.getProperty("arangodb.password"))
                .build();
    }

    /**
     * Get a singleton DB instance
     * @return The DB instance
     */
    public static ArangoDatabaseHandler getDBConnection() {
        if(dbConnection == null)
            dbConnection = new ArangoDatabaseHandler();
        return dbConnection;
    }

    public ArangoDB getArangoDriver() {
        return arangoDriver;
    }


    /**
     * Connects to ArangoDB
     */
    public void connect(){
        String databaseName = configProps.getProperty("db.name");
        String collectionName = configProps.getProperty("collection.users.name");

        /* Select driver */
        arangoDriver = getDBConnection().getArangoDriver();

        /* If com.linkedin.replica.signUpInOut.database does not exist */
        if (!arangoDriver.getDatabases().contains(databaseName)) {
            /* Create com.linkedin.replica.signUpInOut.database */
            arangoDriver.createDatabase(databaseName);
            /* Create collection */
            arangoDriver.db(databaseName).createCollection(collectionName);
        }

        /* Select com.linkedin.replica.signUpInOut.database */
        database = arangoDriver.db(databaseName);
        /* Select collection */
        collection = database.collection(collectionName);
    }

    /**
     * Disconnects from ArangoDB
     */
    public void disconnect() {
        /* Tear down connection */
        arangoDriver.shutdown();
    }

    /**
     * Creates user profile
     * @return string key for entry in com.linkedin.replica.signUpInOut.database
     */
    public String createUser(Object user) {
        UserProfile userProfile= (UserProfile) user;

        DocumentCreateEntity insertedUserProfile = collection.insertDocument(userProfile);
        return insertedUserProfile.getKey();
    }


    /**
     * Retrieves user profile by arango com.linkedin.replica.signUpInOut.database key
     * @return UserProfile
     */
    public Object getUser(String key) {
        /* Retrieve user profile */
//        return collection.getDocument(key, UserProfile.class);
        return collection.getDocument(key, UserProfile.class);
    }

    //TODO: Delete Later
    public static void main(String[] args) {
        ArangoDatabaseHandler handler = new ArangoDatabaseHandler();
        handler.connect();

        UserProfile userProfile = UserProfile.Instantiate();

//        userProfile.setKey("66");
        userProfile.setEmail("nabila.ahmed@gmail.com");
        userProfile.setFirstName("Nabila");
        userProfile.setLastName("Ahmed");

        String key = handler.createUser(userProfile);

        System.out.println(key);
        System.out.println(handler.getUser(key));

        handler.disconnect();
    }
}
