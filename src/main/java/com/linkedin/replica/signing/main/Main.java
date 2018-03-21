package com.linkedin.replica.signing.main;

import com.linkedin.replica.signing.config.Configuration;
import com.linkedin.replica.signing.database.DatabaseConnection;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

public class Main {
    public void start(String... args) throws ClassNotFoundException, IOException, SQLException {
        if (args.length != 4)
            throw new IllegalArgumentException("Expected four arguments. 1- App config file path "
                    + "2- Arango config file path 3- Mysql config file path 4- Command config file path");

        // create singleton instance of Configuration class that will hold configuration files paths
        Configuration.init(args[0], args[1], args[2], args[3]);

        // create singleton instance of DatabaseConnection class that is responsible for making connections with databases
        DatabaseConnection.init();
    }

    public void shutdown() throws ClassNotFoundException, IOException, SQLException {
        DatabaseConnection.getInstance().closeConnections();
    }

    public static void main(String[] args) throws IOException, ParseException, SQLException, ClassNotFoundException {
        new Main().start(args);
    }
}
