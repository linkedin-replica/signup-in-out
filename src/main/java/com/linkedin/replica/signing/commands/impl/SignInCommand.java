package com.linkedin.replica.signing.commands.impl;

import com.linkedin.replica.signing.commands.Command;
import com.linkedin.replica.signing.database.handlers.SigningHandler;
import com.linkedin.replica.signing.models.User;
import com.linkedin.replica.signing.utils.JwtUtils;
import com.linkedin.replica.signing.utils.SHA512;

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
     * @return Response with result and error message if an error occurs
     */

    public LinkedHashMap<String, Object> execute() {
        validateArgs(new String[]{"email", "password"});

        SigningHandler signingHandler = (SigningHandler) dbHandlers.get("sql");
        LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
        String errMsg;

        String email = (String) args.get("email");
        String password = SHA512.hash((String) args.get("password"));
        User user = signingHandler.getUser(email);

        if (user != null) {
            if (user.getPassword().equals(password)) {
                Map<String, Object> claims = new HashMap<String, Object>();
                claims.put("email", user.getEmail());
                claims.put("scope", "self/groups/admins");
                response = JwtUtils.generateToken(claims, user.getId(),
                        60);
                return response;
            } else
                errMsg = "Incorrect password";
        } else
            errMsg = "No such user";
        response.put("success", false);
        response.put("errMsg", errMsg);
        return response;
    }
}
