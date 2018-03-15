package com.linkedin.replica.signing.commands.impl;

import com.linkedin.replica.signing.commands.Command;
import com.linkedin.replica.signing.utils.JwtUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class SignOutCommand extends Command {


    public SignOutCommand(HashMap<String, Object> args) {
        super(args);
    }

    /**
     * Checks validity of token
     * TODO: Don't propagate the respond from JWT.
     *
     * @return Return success boolean in response
     */

    public LinkedHashMap<String, Object> execute() {
        validateArgs(new String[]{"jwtToken"});
        LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();

        String jwtToken = (String) args.get("jwtToken");

        if (jwtToken == null) {
            response.put("errMsg", "Failure due to missing parameters while signing out");
            response.put("success", false);
        } else
            response = JwtUtils.validateToken(jwtToken);

        return response;
    }
}