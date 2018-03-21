package com.linkedin.replica.signing.tests;
import com.linkedin.replica.signing.database.handlers.impl.MysqlSigningHandler;
import com.linkedin.replica.signing.models.User;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MysqlSigningHandlerTest {
//    private static MysqlSigningHandler mysqlDatabaseHandler;
//
//    @BeforeClass
//    public static void setup()  {
//        mysqlDatabaseHandler = new MysqlSigningHandler();
//        mysqlDatabaseHandler.deleteAll();
//        mysqlDatabaseHandler.connect();
//
//    }
//
//    @Test
//    public void testCreateUser() {
//        String email = "Esraa.Khaled@golokoz.com";
//        String password = "SokarNbat";
//
//        String userId = mysqlDatabaseHandler.createUser(email, password);
//        User user = mysqlDatabaseHandler.getUserWithId(userId);
//        assertEquals(String.format("The user of email: %s should exist in Users table", email), user.getEmail(), email);
//
//        String duplicateUserId = mysqlDatabaseHandler.createUser(email, password);
//        assertEquals("Expected that the duplicate has the same id of the old one without exception", duplicateUserId, userId);
//
//    }
//
//    @Test
//    public void testGetUser() {
//
//        String email = "Esraa.Khaled@golokoz.com";
//        String password = "SokarNbat";
//
//        User user = new User("Esraa.Khaled@golokoz.com", "SokarNbat");
//        mysqlDatabaseHandler.createUser(user);
//
//        User newUser = (User) mysqlDatabaseHandler.getUser(email);
//        assertEquals(String.format("Expected both users have the same email", email), email, newUser.getEmail());
//        assertEquals(String.format("Expected both users have the same password", password), password, newUser.getPassword());
//
//    }
//
//
//    @AfterClass
//    public static void teardown() {
//        mysqlDatabaseHandler.disconnect();
//    }
}
