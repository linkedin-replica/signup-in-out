package com.linkedin.replica.signUpInOut.commands;

import com.linkedin.replica.signUpInOut.database.handlers.DatabaseHandler;

import java.util.HashMap;
import java.util.LinkedHashMap;

public abstract class Command {
    protected HashMap<String, String> args;
    protected HashMap<String, DatabaseHandler> dbHandlers;

    public Command(HashMap<String, String> args) {
        this.args = args;
        //TODO: remove this line. the service will call it instead !! only leave on com.linkedin.replica.signUpInOut.tests
//        setDbHandlers(new MysqlDatabaseHandler(), new ArangoDatabaseHandler());
    }

    public void setDbHandlers(DatabaseHandler sqldbHandler, DatabaseHandler nosqldbHandler) {
        dbHandlers = new HashMap<String, DatabaseHandler>();
        dbHandlers.put("sqldbHandler", sqldbHandler);
        dbHandlers.put("nosqldbHandler", nosqldbHandler);
    }

        /**
         * Execute the command
         *
         * @return The output (if any) of the command
         */
    public abstract LinkedHashMap<String, Object> execute();
}
