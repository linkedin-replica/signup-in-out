package com.linkedin.replica.signUpInOut.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static final String ConfigPath = "src/main/resources/config/";

    private static final Logger LOGGER = LogManager.getLogger(ConfigReader.class.getName());

    public static Properties readDatabaseConfig(){
        return readConfig(ConfigPath + "database.config");
    }

    public static Properties readCommandConfig(){
        return readConfig(ConfigPath + "commands.config");
    }
    public static Properties readAppConfig(){
        return readConfig(ConfigPath + "app.config");
    }

    public static Properties readConfig(String filePath){
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(filePath));
        } catch (IOException e) {
            LOGGER.fatal("Couldn't find config file");
            e.printStackTrace();
        }
        return properties;
    }
}
