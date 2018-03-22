package com.linkedin.replica.signing.tests;

import com.linkedin.replica.signing.models.User;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

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
                user.setId("" + results.getInt("id"));
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
            CallableStatement statement = dbInstance.prepareCall("{CALL Insert_User(?, ?)}");
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getPassword());
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
}
