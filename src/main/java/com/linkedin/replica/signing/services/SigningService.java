package com.linkedin.replica.signing.services;


import com.linkedin.replica.signing.commands.Command;
import com.linkedin.replica.signing.config.Configuration;
import com.linkedin.replica.signing.database.handlers.DatabaseHandler;

import java.lang.reflect.Constructor;
import java.util.HashMap;

public class SigningService {
    private Configuration config;


    public SigningService() {
        config = Configuration.getInstance();
    }

    public Object serve(String commandName, HashMap<String, String> args) throws Exception {
        Class<?> commandClass = config.getCommandClass(commandName);
        Constructor constructor = commandClass.getConstructor(HashMap.class);
        Command command = (Command) constructor.newInstance(args);

        if (commandName.equals("signing.signIn") || commandName.equals("signing.signUp")) {
            Class<?> dbHandlerClass = config.getDatabaseHandlerClass(commandName + ".SQL");
            command.addHandler("SQL", (DatabaseHandler) dbHandlerClass.newInstance());
        }

        if (commandName.equals("signing.signUp")) {
            Class<?> dbHandlerClass = config.getDatabaseHandlerClass(commandName + ".noSQL");
            command.addHandler("noSQL", (DatabaseHandler) dbHandlerClass.newInstance());
        }
        return command.execute();
    }
}

