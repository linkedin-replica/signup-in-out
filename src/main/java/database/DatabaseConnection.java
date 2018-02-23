package database;

import com.arangodb.ArangoDB;
import utils.ConfigReader;

import java.io.IOException;
import java.util.Properties;

import static utils.ConfigReader.readConfig;

/**
 * A singleton class carrying a database instance
 */
public class DatabaseConnection {
    private ArangoDB arangoDriver;
    private Properties configProps;

    private static DatabaseConnection dbConnection;

    private DatabaseConnection() {
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
    public static DatabaseConnection getDBConnection() {
        if(dbConnection == null)
            dbConnection = new DatabaseConnection();
        return dbConnection;
    }


    public ArangoDB getArangoDriver() {
        return arangoDriver;
    }
}
