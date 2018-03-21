package com.linkedin.replica.signing.config;

import com.linkedin.replica.signing.commands.Command;
import com.linkedin.replica.signing.database.handlers.SigningHandler;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Configuration {
    private final Properties commandConfig = new Properties();
    private final Properties appConfig = new Properties();
    private final Properties arangoConfig = new Properties();
    private final Properties mysqlConfig = new Properties();

    private static Configuration instance;

    private Configuration(String appConfigPath, String arangoConfigPath, String mysqlConfigPath, String commandsConfigPath) throws IOException {
        populateWithConfig(appConfigPath, appConfig);
        populateWithConfig(arangoConfigPath, arangoConfig);
        populateWithConfig(mysqlConfigPath, mysqlConfig);
        populateWithConfig(commandsConfigPath, commandConfig);
    }

    private Configuration(String configPath) throws IOException {
        populateWithConfig(configPath + "app.config", appConfig);
        populateWithConfig(configPath + "arango.config", arangoConfig);
        populateWithConfig(configPath + "mysql.config", mysqlConfig);
        populateWithConfig(configPath + "command.config", commandConfig);
    }

    public static Configuration getInstance() {
        return instance;
    }

    /**
     * Creates a static instance of properties
     */
    public static void init(String appConfigPath, String arangoConfigPath, String mysqlConfigPath, String commandsConfigPath) throws IOException {
        instance = new Configuration(appConfigPath, arangoConfigPath, mysqlConfigPath, commandsConfigPath);
    }

    public static void init(String configPath) throws IOException {
        instance = new Configuration(configPath);
    }


    private static void populateWithConfig(String configFilePath, Properties properties) throws IOException, FileNotFoundException {
        FileInputStream inputStream = new FileInputStream(configFilePath);
        properties.load(inputStream);
        inputStream.close();
    }

    public Class getCommandClass(String commandName) throws ClassNotFoundException {
        String commandsPackageName = Command.class.getPackage().getName() + ".impl";
        String commandClassPath = commandsPackageName + '.' + commandConfig.get(commandName);
        return Class.forName(commandClassPath);
    }

    public Class getDatabaseHandlerClass(String commandName) throws ClassNotFoundException {
        String handlerPackageName = SigningHandler.class.getPackage().getName() + ".impl";
        String handlerClassPath = handlerPackageName + "." + commandConfig.get(commandName + ".handler");
        return Class.forName(handlerClassPath);
    }

    public String getAppConfigProp(String key) {
        return appConfig.getProperty(key);
    }

    public String getArangoConfigProp(String key) {
        return arangoConfig.getProperty(key);
    }

    public String getMysqlConfigProp(String key) {
        return mysqlConfig.getProperty(key);
    }

}