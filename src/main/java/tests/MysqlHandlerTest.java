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
        mysqlHandler.connect();
    }

    @Test
    public void testCreateUser() {
        String email = "Esraa.Khaled@golokoz.com";
        String password = "SokarNbat";
        User.delete("email = ?", email);

        String userId = mysqlHandler.createUser(email, password);
        assertEquals(String.format("The user of email: %s should exist in Users table", email), User.exists(userId), true);

        String duplicateUserId = mysqlHandler.createUser(email, password);
        assertEquals("Expected that the duplicate has the same id of the old one without exception", duplicateUserId, userId);

        User.delete("id = ?", userId);
        assertEquals("The user should be deleted",User.exists(userId), false);
    }

    @Test
    public void testGetUser() {
        String email = "Esraa.Khaled@golokoz.com";
        String password = "SokarNbat";
        User.delete("email = ?", email);

        User user = new User();
        user.set("email", email);
        user.set("password", password);
        user.saveIt();

        User newUser = (User) mysqlHandler.getUser(email);
        assertEquals(String.format("Expected both users have the same email", email), email, newUser.get("email"));
        assertEquals(String.format("Expected both users have the same password", password), password, newUser.get("password"));

        User.delete("email = ?", email);
        assertEquals("The user should be deleted",User.exists(user.getId()), false);
    }

    @Test
    public void testDeleteAll() {
        mysqlHandler.deleteAll();
        assertEquals("Expected zero record in the Users table",(long) User.count(), 0L);
    }

    @AfterClass
    public static void teardown() {
        mysqlHandler.disconnect();
    }
}
