package services;

import utils.ConfigReader;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Properties;

public class UserAuthenticationService {

    private Properties config;

    public UserAuthenticationService() {
        config = ConfigReader.readCommandConfig();
    }


//    public LinkedHashMap<String, Object> serve(String commandName, HashMap<String, String> args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
//        String commandClassString = config.getProperty(commandName);
//        Class<?> commandClass = commandClassString.getClass();
//        Constructor constructor = commandClass.getConstructor(HashMap.class);
//        Command command = (Command) constructor.newInstance(args);
//
//        Class<?> noSqlHandlerClass = config.getNoSqlHandler();
//        DatabaseHandler noSqlHandler = (DatabaseHandler) noSqlHandlerClass.newInstance();
//
//        command.setDbHandler(noSqlHandler);
//
//        return command.execute();
//    }
}
