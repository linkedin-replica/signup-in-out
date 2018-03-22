package com.linkedin.replica.signing.commands.impl;

import com.linkedin.replica.signing.commands.Command;
import com.linkedin.replica.signing.database.handlers.SigningHandler;
import com.linkedin.replica.signing.exceptions.SigningException;
import com.linkedin.replica.signing.models.User;
import com.linkedin.replica.signing.utils.JwtUtils;
import com.linkedin.replica.signing.utils.SHA512;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class SignInCommand extends Command {

    public SignInCommand(HashMap<String, Object> args) {
        super(args);
    }


    /**
     * Handle the sign in process and generate the JWT token to be used in authentication
     *
     * @return Response with jwtToken and error message if an error occurs
     */

    public String execute() throws SQLException, UnsupportedEncodingException {
        validateArgs(new String[]{"email", "password"});

        SigningHandler signingHandler = (SigningHandler) dbHandler;
        String email = (String) args.get("email");
        String password = SHA512.hash((String) args.get("password"));
        User user = signingHandler.getUser(email);

        if (user != null && user.getPassword().equals(password)) {
            Map<String, Object> claims = new HashMap<String, Object>();
            claims.put("email", user.getEmail());
            claims.put("scope", "self/groups/admins");
            return JwtUtils.generateToken(claims, user.getId(),
                    60);
        } else
            throw new SigningException("Incorrect username/password");
    }
}
