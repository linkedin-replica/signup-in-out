package com.linkedin.replica.signing.commands;

import com.linkedin.replica.signing.database.handlers.DatabaseHandler;
import com.linkedin.replica.signing.exceptions.BadRequestException;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;

public abstract class Command {
    protected HashMap<String, Object> args;
    protected DatabaseHandler dbHandler;


    public Command(HashMap<String, Object> args) {
        this.args = args;
    }

    public void addDatabaseHandler(DatabaseHandler dbHandler) {
        this.dbHandler = dbHandler;
    }

    /**
     * Execute the command
     *
     * @return The output (if any) of the command
     */
    public abstract Object execute() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, SQLException, UnsupportedEncodingException;

    protected void validateArgs(String[] requiredArgs) {
        for (String arg : requiredArgs)
            if (!args.containsKey(arg) || args.get(arg) == null) {
                String exceptionMsg = String.format("Cannot execute command. %s argument is missing", arg);
                throw new BadRequestException(exceptionMsg);
            }
    }
}
