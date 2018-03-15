package com.linkedin.replica.signing.commands.impl;

import com.linkedin.replica.signing.commands.Command;
import com.linkedin.replica.signing.database.handlers.SigningHandler;
import com.linkedin.replica.signing.models.User;
import com.linkedin.replica.signing.utils.SHA512;
import org.apache.commons.validator.routines.EmailValidator;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class SignUpCommand extends Command {


    public SignUpCommand(HashMap<String, Object> args) {
        super(args);
    }

    /**
     * Validates user profile
     */
    public boolean isValidUserEmail(String email) {
        // Check if valid email
        return EmailValidator.getInstance().isValid(email);
    }

    /**
     * Handle the sign up process and generate the ID of the inserted user to be used accross com.linkedin.replica.signing.services
     *
     * @return Response with result and error message if an error occurs
     */
    public LinkedHashMap<String, Object> execute() {
        validateArgs(new String[]{"email", "password", "firstName", "lastName"});
        SigningHandler signingSqlHandler = (SigningHandler) dbHandlers.get("SQL");
        SigningHandler signingNoSqlHandler = (SigningHandler) dbHandlers.get("noSQL");

        LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
        String errMsg = "";
        Boolean success = false;


        String email = (String) args.get("email");
        String password = SHA512.hash((String) args.get("password"));

        User user = signingSqlHandler.getUser(email);

        if (user != null) {
            errMsg = "This user already exists, Do you want to sign in?";
        } else if (!isValidUserEmail(email)) {
            errMsg = "Invalid Email";
        } else {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setPassword(password);
            String id = signingSqlHandler.createUser(newUser); // use this key as the id of Userprofile object

            newUser.setFirstName((String) args.get("firstName"));
            newUser.setLastName((String) args.get("lastName"));
            newUser.setId(id);

            signingNoSqlHandler.createUser(newUser);
            success = true;
            response.put("userId", id);
        }
        response.put("success", success);
        response.put("errMsg", errMsg);
        return response;
    }
}