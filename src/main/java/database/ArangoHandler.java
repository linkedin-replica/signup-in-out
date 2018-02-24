package database;

import com.arangodb.ArangoCollection;
import com.arangodb.ArangoDB;
import com.arangodb.ArangoDatabase;
import com.arangodb.entity.DocumentCreateEntity;
import model.UserProfile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Properties;
import static utils.ConfigReader.readConfig;

public class ArangoHandler implements DatabaseHandler{

    private static final Logger LOGGER = LogManager.getLogger(ArangoHandler.class.getName());

    private Properties configProps;
    private ArangoDB arangoDriver;
    private ArangoDatabase database;
    private ArangoCollection collection;
    private static ArangoHandler dbConnection;


    public ArangoHandler(){
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
    public static ArangoHandler getDBConnection() {
        if(dbConnection == null)
            dbConnection = new ArangoHandler();
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

        /* If database does not exist */
        if (!arangoDriver.getDatabases().contains(databaseName)) {
            /* Create database */
            arangoDriver.createDatabase(databaseName);
            /* Create collection */
            arangoDriver.db(databaseName).createCollection(collectionName);
            System.out.println("Database created");
        }

        /* Select database */
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
     */
    public String createUser(Object user) {
        UserProfile userProfile= (UserProfile) user;

        DocumentCreateEntity insertedUserProfile = collection.insertDocument(userProfile);
        return insertedUserProfile.getKey();
    }


    /**
     * Retrieves user profile by key
     */
    public Object getUser(String id) {
        /* Retrieve user profile */
        return collection.getDocument(id, UserProfile.class);
    }

    //TODO: Delete Later
    public static void main(String[] args) {
        ArangoHandler handler = new ArangoHandler();
        handler.connect();

        UserProfile userProfile = UserProfile.Instantiate();

        userProfile.setEmail("nabila.ahmed@gmail.com");
        userProfile.setFirstName("Nabila");
        userProfile.setLastName("Ahmed");

        String key = handler.createUser(userProfile);

        System.out.println(handler.getUser(key));

        handler.disconnect();
    }
}
