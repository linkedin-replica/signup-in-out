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
     * ATTENTION!!!!!
     * The token will be already validated in the netty server
     * this is only here for future redirection or additional stuff
     *
     * @return Return success boolean in response
     */

    public Boolean execute() {
        validateArgs(new String[]{"jwtToken"});
        return JwtUtils.validateToken((String) args.get("jwtToken"));
    }
}