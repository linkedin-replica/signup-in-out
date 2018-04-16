package com.linkedin.replica.signing.database;

import com.arangodb.ArangoDB;
import com.linkedin.replica.signing.config.Configuration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * A singleton class for the connections of databases (Arango, Mysql).
 */
public class DatabaseConnection {
    private ArangoDB arangoDriver;
    private Connection mysqlDriver;
    private Configuration config;

    private static DatabaseConnection instance;

    private DatabaseConnection() throws IOException, SQLException {
        config = Configuration.getInstance();
        initializeArangoDB();
        initializeMysqlDB();
    }

    /**
     * @return A singleton database instance
     */
    public static DatabaseConnection getInstance() {
        return instance;
    }

    public static void init() throws IOException, SQLException {
        instance = new DatabaseConnection();
    }

    private void initializeArangoDB() {
        arangoDriver = new ArangoDB.Builder()
                .host(config.getArangoConfigProp("arangodb.host"),
                        Integer.parseInt(config.getArangoConfigProp("arangodb.port")))
                .user(config.getArangoConfigProp("arangodb.user"))
                .password(config.getArangoConfigProp("arangodb.password"))
                .build();
    }

    private void initializeMysqlDB() throws SQLException {
        mysqlDriver = DriverManager.getConnection(config.getMysqlConfigProp("mysql.url"),
                config.getMysqlConfigProp("mysql.username"),
                config.getMysqlConfigProp("mysql.password"));
    }


    public void closeConnections() throws SQLException {
        mysqlDriver.close();
        arangoDriver.shutdown();
    }

    public ArangoDB getArangoDriver() {
        return arangoDriver;
    }

    public Connection getMysqlDriver() {
        return mysqlDriver;
    }
}