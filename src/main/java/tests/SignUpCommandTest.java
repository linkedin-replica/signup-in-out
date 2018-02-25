package tests;

import abstraction.Command;
import com.arangodb.ArangoDatabase;
import database.ArangoHandler;
import database.MysqlHandler;
import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import userCommands.SignInCommand;
import utils.SHA512;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Properties;

import static org.junit.Assert.*;

public class SignUpCommandTest {
    private static MysqlHandler mysqlHandler;
    private static Command command;
    private static ArangoHandler arangoHandler;
    private static ArangoDatabase arangoDb;
    static Properties config;


}
