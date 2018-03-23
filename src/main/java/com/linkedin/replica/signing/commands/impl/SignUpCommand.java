package com.linkedin.replica.signing.commands.impl;

import com.linkedin.replica.signing.commands.Command;
import com.linkedin.replica.signing.database.handlers.SigningHandler;
import com.linkedin.replica.signing.exceptions.SigningException;
import com.linkedin.replica.signing.models.User;
import com.linkedin.replica.signing.utils.SHA512;
import org.apache.commons.validator.routines.EmailValidator;

import java.sql.SQLException;
import java.util.HashMap;

public class SignUpCommand extends Command {


    public SignUpCommand(HashMap<String, Object> args) {
        super(args);
    }

    /**
     * Validates user mail
     */
    public boolean isValidUserEmail(String email) {
        return EmailValidator.getInstance().isValid(email);
    }

    /**
     * Handle the sign up process and generate the ID of the inserted user to be used accross com.linkedin.replica.signing.services
     *
     * @return Respond with true if the user is created and error message otherwise
     */
    public Boolean execute() throws SQLException {
        validateArgs(new String[]{"email", "password", "firstName", "lastName"});

        SigningHandler signingHandler = (SigningHandler) dbHandler;
        String email = (String) args.get("email");
        String password = SHA512.hash((String) args.get("password"));

        if (!isValidUserEmail(email))
            throw new SigningException("Invalid email");
        else if (signingHandler.getUser(email) != null)
            throw new SigningException("This user already exists, Do you want to sign in?");
        else {
            User user = new User();
            user.setEmail(email);
            user.setPassword(password);
            user.setFirstName((String) args.get("firstName"));
            user.setLastName((String) args.get("lastName"));
            signingHandler.createUser(user);
            return true;
        }
    }
}