package com.linkedin.replica.signing.database.handlers.impl;

import com.arangodb.ArangoCollection;
import com.linkedin.replica.signing.config.Configuration;
import com.linkedin.replica.signing.database.DatabaseConnection;
import com.linkedin.replica.signing.database.handlers.SigningHandler;
import com.linkedin.replica.signing.models.User;

public class ArangoSigningHandler implements SigningHandler {

    private ArangoCollection collection;

    public ArangoSigningHandler() {
        Configuration config = Configuration.getInstance();
        collection = DatabaseConnection.getInstance().getArangoDriver().
                db(config.getArangoConfig("db.name"))
                .collection(config.getArangoConfig("collection.name"));
    }

    public String createUser(User user) {
        return collection.insertDocument(user).getKey();
    }

    public User getUser(String key) {
        return collection.getDocument(key, User.class);
    }
}
