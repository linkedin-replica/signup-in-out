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
    private ArangoDB driver;
    private ArangoDatabase database;
    private ArangoCollection collection;

    public ArangoHandler(){
        configProps = readConfig();
    }
    /**
     * Connects to ArangoDB
     */
    public void connect(){
        String databaseName = configProps.getProperty("db.name");
        String collectionName = configProps.getProperty("collection.users.name");

        /* Select driver */
        driver = DatabaseConnection.getDBConnection().getArangoDriver();

        /* If database does not exist */
        if (!driver.getDatabases().contains(databaseName)) {
            /* Create database */
            driver.createDatabase(databaseName);
            /* Create collection */
            driver.db(databaseName).createCollection(collectionName);
            System.out.println("Database created");
        }

        /* Select database */
        database = driver.db(databaseName);
        /* Select collection */
        collection = database.collection(collectionName);
    }

    /**
     * Disconnects from ArangoDB
     */
    public void disconnect() {
        /* Tear down connection */
        driver.shutdown();
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
