package tests;
import database.MysqlHandler;
import model.User;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.assertEquals;

public class MysqlHandlerTest {
    private static MysqlHandler mysqlHandler;

    @BeforeClass
    public static void setup()  {
        mysqlHandler = new MysqlHandler();
        mysqlHandler.deleteAll();
        mysqlHandler.connect();

    }

    @Test
    public void testCreateUser() {
        String email = "Esraa.Khaled@golokoz.com";
        String password = "SokarNbat";

        String userId = mysqlHandler.createUser(email, password);
        User user = mysqlHandler.getUserWithId(userId);
        assertEquals(String.format("The user of email: %s should exist in Users table", email), user.getEmail(), email);

        String duplicateUserId = mysqlHandler.createUser(email, password);
        assertEquals("Expected that the duplicate has the same id of the old one without exception", duplicateUserId, userId);

    }

    @Test
    public void testGetUser() {

        String email = "Esraa.Khaled@golokoz.com";
        String password = "SokarNbat";

        User user = new User("Esraa.Khaled@golokoz.com", "SokarNbat");
        mysqlHandler.createUser(user);

        User newUser = (User) mysqlHandler.getUser(email);
        assertEquals(String.format("Expected both users have the same email", email), email, newUser.getEmail());
        assertEquals(String.format("Expected both users have the same password", password), password, newUser.getPassword());

    }


    @AfterClass
    public static void teardown() {
        mysqlHandler.disconnect();
    }
}
