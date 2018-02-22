package database;


import com.arangodb.ArangoCollection;
import com.arangodb.ArangoDB;
import com.arangodb.ArangoDatabase;
import model.User;
import utils.ConfigReader;

import java.io.IOException;
import java.util.List;

public class ArangoHandler implements DatabaseHandler{

    private ConfigReader config;
    private ArangoDatabase dbInstance;
    private ArangoCollection collection;
    private String collectionName;

    private static DatabaseConnection dbConnection;

    public ArangoHandler() throws IOException {
        // read arango constants
        config = new ConfigReader("config");

        // init db
        ArangoDB arangoDriver = DatabaseConnection.getDBConnection().getArangoDriver();
        collectionName = config.getConfig("collection.users.name");
        dbInstance = arangoDriver.db(config.getConfig("db.name"));
        collection = dbInstance.collection(collectionName);
    }

    public void connect() {
        // TODO
    }



    public void disconnect() {
        // TODO
    }


    public User getUser(String email) {

        return null;
    }

    public void createUser(String email, String password) {

    }

    public static void main(String[] args) throws Exception {
        ArangoHandler a = new ArangoHandler();


    }

}