package com.linkedin.replica.signing.database.handlers.impl;

import com.arangodb.ArangoDatabase;
import com.linkedin.replica.signing.config.Configuration;
import com.linkedin.replica.signing.database.DatabaseConnection;
import com.linkedin.replica.signing.database.handlers.SigningHandler;
import com.linkedin.replica.signing.models.User;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ArangoSqlSigningHandler implements SigningHandler {
    private ArangoDatabase arangoDatabase;
    private Connection mySqlDbInstance;
    private Configuration config;

    public ArangoSqlSigningHandler() {
        mySqlDbInstance = DatabaseConnection.getInstance().getMysqlDriver();
        config = Configuration.getInstance();
        arangoDatabase = DatabaseConnection.getInstance().getArangoDriver().
                db(config.getArangoConfigProp("db.name"));
    }


    @Override
    public User getUser(String email) throws SQLException {
        User user = null;
        CallableStatement statement = mySqlDbInstance.prepareCall("{CALL search_for_user(?)}");
        statement.setString(1, email);
        statement.executeQuery();
        ResultSet results = statement.getResultSet();
        if (results.next()) {
            user = new User();
            user.setId("" + results.getInt("id"));
            user.setEmail(results.getString("email"));
            user.setPassword(results.getString("password"));
        }
        return user;
    }

    @Override
    public String createUser(User user) throws SQLException {
        CallableStatement statement = mySqlDbInstance.prepareCall("{CALL Insert_User(?, ?)}");
        statement.setString(1, user.getEmail());
        statement.setString(2, user.getPassword());
        statement.executeQuery();
        user.setId(getUser(user.getEmail()).getId());
        return arangoDatabase.collection(config.getArangoConfigProp("collection.name")).insertDocument(user).getKey();
    }
}