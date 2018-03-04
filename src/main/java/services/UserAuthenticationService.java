package services;

import abstraction.Command;
import database.DatabaseHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.ConfigReader;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Properties;

public class UserAuthenticationService {

    private Properties config;
    private Properties appConfig;

    private static final Logger LOGGER = LogManager.getLogger(UserAuthenticationService.class.getName());


    public UserAuthenticationService() {
        config = ConfigReader.readCommandConfig();
        appConfig = ConfigReader.readAppConfig();
    }

    public LinkedHashMap<String, Object> serve(String commandName, HashMap<String, String> args) {
        String commandClassString  = config.getProperty(commandName);
        String commandsPackageName = appConfig.getProperty("package.commands");
        String commandClassFullStr = commandsPackageName + '.' + commandClassString;

        try{
            Class<?> commandClass= Class.forName(commandClassFullStr);
            Constructor constructor = commandClass.getConstructor(HashMap.class);
            Command command = (Command) constructor.newInstance(args);

            String handlersPackageName = appConfig.getProperty("package.handlers");
            Class<?> noSqlHandlerClass = Class.forName(handlersPackageName + '.' + appConfig.get("handler.nosql"));
            DatabaseHandler noSqlHandler = (DatabaseHandler) noSqlHandlerClass.newInstance();

            Class<?> sqlHandlerClass = Class.forName(handlersPackageName + '.' + appConfig.get("handler.sql"));
            DatabaseHandler sqlHandler = (DatabaseHandler) sqlHandlerClass.newInstance();

            command.setDbHandlers(sqlHandler, noSqlHandler); // @TODO: Fix that both database handlers are added in all commands

            return command.execute();

        }catch (ClassNotFoundException e){
            LOGGER.fatal("Couldn't find Class");
            e.printStackTrace();

        }catch (NoSuchMethodException e){
            LOGGER.fatal("Couldn't find called method");
            e.printStackTrace();

        }catch (InvocationTargetException e){
            LOGGER.fatal("Couldn't find config file");
            e.printStackTrace();

        }catch (IllegalAccessException e){
            LOGGER.fatal("Couldn't access class");
            e.printStackTrace();

        }catch (InstantiationException e){
            LOGGER.warn("Couldn't Instantiate handler");
            e.printStackTrace();

        }catch (Exception e) {
            LOGGER.warn("Exception thrown in class UserAuthenticationService");
            e.printStackTrace();
        }finally {
            return null;
        }
    }
}
