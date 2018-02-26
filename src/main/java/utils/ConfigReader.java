package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static final String FilePath = "resources/config";

    private static final Logger LOGGER = LogManager.getLogger(ConfigReader.class.getName());

    public static Properties readConfig(){
        return readConfig(FilePath);
    }
    public static Properties readCommandConfig(){
        return readConfig("resources/commands.config");
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
