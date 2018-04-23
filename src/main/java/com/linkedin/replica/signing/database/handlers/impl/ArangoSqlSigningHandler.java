package com.linkedin.replica.signing.database.handlers.impl;

import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDatabase;
import com.arangodb.util.MapBuilder;
import com.linkedin.replica.signing.config.Configuration;
import com.linkedin.replica.signing.database.DatabaseConnection;
import com.linkedin.replica.signing.database.handlers.SigningHandler;
import com.linkedin.replica.signing.models.LoggedInUser;
import com.linkedin.replica.signing.models.User;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

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
            user.setId(results.getString("id"));
            user.setEmail(results.getString("email"));
            user.setPassword(results.getString("password"));
        }
        return user;
    }

    @Override
    public String createUser(User user) throws SQLException {
        CallableStatement statement = mySqlDbInstance.prepareCall("{CALL Insert_User(?, ?, ?)}");
        statement.setString(1, user.getEmail());
        statement.setString(2, user.getPassword());
        statement.setString(3, user.getId());
        statement.executeQuery();
        user.setId(getUser(user.getEmail()).getId());
        return arangoDatabase.collection(config.getArangoConfigProp("collection.name")).insertDocument(user).getKey();
    }

    public LoggedInUser getLoggedInUser(String userId) {
        String query = "FOR user IN users " +
                "FILTER user.userId == @id " +
                "return {\"userId\": user.userId, \"name\": CONCAT(user.firstName, \" \", user.lastName), \"profilePictureUrl\": user.profilePictureUrl}";
        Map<String, Object> bindVars = new MapBuilder().put("id", userId).get();

        ArangoCursor<LoggedInUser> cursor = arangoDatabase.query(query, bindVars, null, LoggedInUser.class);
        LoggedInUser loggedInUser = cursor.next();
        return loggedInUser;
    }
}