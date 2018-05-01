package com.linkedin.replica.signing.commands.impl;

import com.linkedin.replica.signing.commands.Command;
import com.linkedin.replica.signing.database.handlers.SigningHandler;
import com.linkedin.replica.signing.models.LoggedInUser;


import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.HashMap;

public class GetLoggedInUserInfo extends Command{

    public GetLoggedInUserInfo(HashMap<String, Object> args) {
        super(args);
    }


    /**
     * get logged in user info by userId
     *
     * @return part of user info (id, name and profilePictureUrl)
     */

    public LoggedInUser execute() throws SQLException, UnsupportedEncodingException {
        validateArgs(new String[]{"userId"});
        SigningHandler signingHandler = (SigningHandler) dbHandler;
        String userId = (String) args.get("userId");
        LoggedInUser loggedInUser = signingHandler.getLoggedInUser(userId);
        return loggedInUser;
    }
}
