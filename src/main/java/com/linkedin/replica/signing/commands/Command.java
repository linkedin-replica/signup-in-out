package com.linkedin.replica.signing.commands;

import com.linkedin.replica.signing.database.handlers.DatabaseHandler;
import com.linkedin.replica.signing.database.handlers.SigningHandler;

import java.util.HashMap;
import java.util.LinkedHashMap;

public abstract class Command {
    protected HashMap<String, Object> args;
    protected HashMap<String, DatabaseHandler> dbHandlers;


    public Command(HashMap<String, Object> args) {
        this.args = args;
    }

    public void addHandler(String dbHandlerType, DatabaseHandler dbHandler) {
        dbHandlers.put(dbHandlerType, dbHandler);
    }

    /**
     * Execute the command
     *
     * @return The output (if any) of the command
     */
    public abstract LinkedHashMap<String, Object> execute();

    protected void validateArgs(String[] requiredArgs) {
        for (String arg : requiredArgs)
            if (!args.containsKey(arg)) {
                String exceptionMsg = String.format("Cannot execute command. %s argument is missing", arg);
                throw new IllegalArgumentException(exceptionMsg);
            }
    }
}
