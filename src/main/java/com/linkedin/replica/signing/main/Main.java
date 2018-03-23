package com.linkedin.replica.signing.main;

import com.linkedin.replica.signing.config.Configuration;
import com.linkedin.replica.signing.controller.Server;
import com.linkedin.replica.signing.database.DatabaseConnection;
import com.linkedin.replica.signing.messaging.ClientMessagesReceiver;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
// src/main/resources/config/app.config src/main/resources/config/arango.config src/main/resources/config/mysql.config src/main/resources/config/command.config src/main/resources/config/controller.config
public class Main {

    private ClientMessagesReceiver clientMessagesReceiver;

    public void start(String... args) throws ClassNotFoundException, IOException, SQLException, InterruptedException {
        if (args.length != 5)
            throw new IllegalArgumentException("Expected four arguments. 1- App config file path "
                    + "2- Arango config file path 3- Mysql config file path 4- Command config file path 5- Controller config file path");

        // create singleton instance of Configuration class that will hold configuration files paths
        Configuration.init(args[0], args[1], args[2], args[3], args[4]);

        // create singleton instance of DatabaseConnection class that is responsible for making connections with databases
        DatabaseConnection.init();

        // start tasks
        Runnable clientMessageRunnable = () -> {
            try {
                clientMessagesReceiver = new ClientMessagesReceiver();
            } catch (Exception e) {
                e.printStackTrace();
                // TODO log
            }
        };

        startTask(clientMessageRunnable, "Client Message Receiver");

        new Server().start();
    }

    private void startTask(Runnable runnable, String name) {
        Thread thread = new Thread(runnable);
        System.out.println("Starting thread " + thread.getId() + " for " + name);
        thread.start();
    }

    public void shutdown() throws ClassNotFoundException, IOException, SQLException {
        DatabaseConnection.getInstance().closeConnections();
    }

    public static void main(String[] args) throws IOException, ParseException, SQLException, ClassNotFoundException, InterruptedException {
        new Main().start(args);
    }
}
