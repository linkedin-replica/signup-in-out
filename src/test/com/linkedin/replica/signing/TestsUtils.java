package com.linkedin.replica.signing;

import com.arangodb.ArangoCollection;
import com.arangodb.entity.CollectionEntity;
import com.arangodb.entity.DocumentCreateEntity;
import com.linkedin.replica.signing.models.User;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class TestsUtils {

    public static User getUserSQL(String email, Connection dbInstance) {
        User user = null;
        try {
            CallableStatement statement = dbInstance.prepareCall("{CALL search_for_user(?)}");
            statement.setString(1, email);
            statement.executeQuery();
            ResultSet results = statement.getResultSet();
            if (results.next()) {
                user = new User();
                user.setId(UUID.randomUUID().toString());
                user.setEmail(results.getString("email"));
                user.setPassword(results.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            return user;
        }
    }

    public static String createUserSQL(User user, Connection dbInstance) {
        try {
            CallableStatement statement = dbInstance.prepareCall("{CALL Insert_User(?, ?, ?)}");
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getId());
            statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            return getUserSQL(user.getEmail(), dbInstance).getId();
        }
    }

    public static void deleteUsersSQL(Connection dbInstance) {
        try {
            CallableStatement statement = dbInstance.prepareCall("{CALL delete_users()}");
            statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteUserSQL(String email, Connection dbInstance) {
        try {
            CallableStatement statement = dbInstance.prepareCall("{CALL delete_user(?)}");
            statement.setString(1, email);
            statement.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static DocumentCreateEntity createUserArango(User user, ArangoCollection collection){
        return collection.insertDocument(user);
    }
    public static void deleteUserArango(String userId, ArangoCollection collection){
        collection.deleteDocument(userId);
    }

    public static User getUserArango(String userId, ArangoCollection collection) {
        return collection.getDocument(userId, User.class);
    }
}
