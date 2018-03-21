package com.linkedin.replica.signing.database.handlers;

import com.linkedin.replica.signing.models.User;

/**
 * An interface that defines the database information that needed to be used in the signing service
 */
public interface SigningHandler extends DatabaseHandler {

    /**
     * Get a user by email
     *
     * @param email The email of the wanted user
     * @return A user object or null
     */
    User getUser(String email);

    /**
     * Create a record of the user if it's not exist
     *
     * @param user
     * @return The user id in the database
     */
    String createUser(User user);

}