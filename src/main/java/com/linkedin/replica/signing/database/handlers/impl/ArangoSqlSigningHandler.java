package com.linkedin.replica.signing.database.handlers.impl;

import com.arangodb.ArangoCollection;
import com.linkedin.replica.signing.config.Configuration;
import com.linkedin.replica.signing.database.DatabaseConnection;
import com.linkedin.replica.signing.database.handlers.SigningHandler;
import com.linkedin.replica.signing.models.User;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ArangoSqlSigningHandler implements SigningHandler {
    private ArangoCollection collection;
    private Connection dbInstance;

    public ArangoSqlSigningHandler() {
        dbInstance = DatabaseConnection.getInstance().getMysqlDriver();
        Configuration config = Configuration.getInstance();
        collection = DatabaseConnection.getInstance().getArangoDriver().
                db(config.getAppConfigProp("db.name"))
                .collection(config.getAppConfigProp("collection.name"));

    }


    @Override
    public User getUser(String email) throws SQLException {
        User user = null;
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
        return user;
    }

    @Override
    public String createUser(User user) throws SQLException {
        CallableStatement statement = dbInstance.prepareCall("{CALL Insert_User(?, ?)}");
        statement.setString(1, user.getEmail());
        statement.setString(2, user.getPassword());
        statement.executeQuery();
        user.setId(getUser(user.getEmail()).getId());
        return collection.insertDocument(user).getKey();
    }
}
