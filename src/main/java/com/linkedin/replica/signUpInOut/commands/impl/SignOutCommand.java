package com.linkedin.replica.signUpInOut.commands.impl;

import com.linkedin.replica.signUpInOut.commands.Command;
import com.linkedin.replica.signUpInOut.utils.JwtUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class SignOutCommand extends Command {

    private static final Logger LOGGER = LogManager.getLogger(SignOutCommand.class.getName());

    public SignOutCommand(HashMap<String, String> args) {
        super(args);
    }

    /**
     * Checks validity of token
     * @return  Return success boolean in response
     */

    public LinkedHashMap<String, Object> execute() {
        LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
        String jwtToken = args.get("jwtToken");

        if(jwtToken == null)
        {
            response.put("errMsg", "Failure due to missing parameters while signing out");
            response.put("success", false);
        }
        else
            response = JwtUtils.validateToken(jwtToken);

        return response;
    }
}