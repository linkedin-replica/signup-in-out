package com.linkedin.replica.signing.database.handlers.impl;

import com.linkedin.replica.signing.database.DatabaseConnection;
import com.linkedin.replica.signing.database.handlers.SigningHandler;
import com.linkedin.replica.signing.models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class MysqlSigningHandler implements SigningHandler {

    private static final Logger LOGGER = LogManager.getLogger(MysqlSigningHandler.class.getName());
    private Connection dbInstance;

    public MysqlSigningHandler() {
        dbInstance = DatabaseConnection.getInstance().getMysqlDriver();
    }

    public User getUser(String email) {
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

    public String createUser(User user) {
        try {
            CallableStatement statement = dbInstance.prepareCall("{CALL Insert_User(?, ?)}");
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getPassword());
            statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            return getUser(user.getEmail()).getId();
        }
    }
}
