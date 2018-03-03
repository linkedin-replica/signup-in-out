package abstraction;

import database.ArangoHandler;
import database.DatabaseHandler;
import database.MysqlHandler;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedHashMap;

public abstract class Command {
    protected HashMap<String, String> args;
    protected HashMap<String, DatabaseHandler> dbHandlers;

    public Command(HashMap<String, String> args) {
        this.args = args;
        setDbHandlers(new MysqlHandler(), new ArangoHandler());
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
