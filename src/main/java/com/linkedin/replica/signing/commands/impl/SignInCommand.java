package com.linkedin.replica.signUpInOut.commands.impl;

import com.linkedin.replica.signUpInOut.commands.Command;
import com.linkedin.replica.signUpInOut.database.handlers.SigningHandler;
import com.linkedin.replica.signUpInOut.database.handlers.impl.MysqlSigningHandler;
import com.linkedin.replica.signUpInOut.models.User;
import com.linkedin.replica.signUpInOut.utils.JwtUtils;
import com.linkedin.replica.signUpInOut.utils.SHA512;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class SignInCommand extends Command {

    private SigningHandler signingHandler;
    private static final Logger LOGGER = LogManager.getLogger(SignInCommand.class.getName());

    public SignInCommand(HashMap<String, String> args) {
        super(args);
        signingHandler = (MysqlSigningHandler) this.dbHandlers.get("sqldbHandler");

    }

    /**
     * Handle the sign in process and generate the JWT token to be used in authentication
     * @return Response with result and error message if an error occurs
     */

    public LinkedHashMap<String, Object> execute() {
        LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();

        String errMsg;

        if(args.containsKey("email") && args.containsKey("password")){

            String email = args.get("email");

            String password = SHA512.hash(args.get("password"));

            signingHandler.connect();
            User user = (User) signingHandler.getUser(email);
            signingHandler.disconnect();

            if(user != null){
                if(user.getPassword().equals(password)){
                    Map<String, Object> claims = new HashMap<String, Object>();
                    claims.put("email", user.getEmail());
                    claims.put("scope", "self/groups/admins");
                    response = JwtUtils.generateToken(claims, user.getId(),
                            60);
                    return response;
                }else
                    errMsg = "Incorrect password";
            }else
                errMsg = "No such user";
        }else
            errMsg = "Missing information";
        response.put("success", false);
        response.put("errMsg", errMsg);
        return response;
    }


}
