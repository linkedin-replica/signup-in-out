package userCommands;

import database.DatabaseHandler;
import database.MysqlHandler;
import model.User;
import modules.JwtUtils;
import modules.SHA512;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

//TODO: add the token to the cache server

public class SignInCommand extends abstraction.Command {

    private DatabaseHandler db;
    private static final Logger LOGGER = LogManager.getLogger(SignInCommand.class.getName());

    public SignInCommand(HashMap<String, String> args) throws IOException {
        super(args);
        db = new MysqlHandler();
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

            db.connect();
            User user = db.getUser(email);
            db.disconnect();

            if(user != null){
                if(user.get("password").equals(password)){
                    Map<String, Object> claims = new HashMap<String, Object>();
                    claims.put("email", user.get("email"));
                    claims.put("scope", "self/groups/admins");
                    response = JwtUtils.generateToken(claims, user.getString("id"), 60);
                    return response;
                }else
                    errMsg = "Incorrect password";
            }else
                errMsg = "No such user";
        }else
            errMsg = "Missing information";

        response.put("errMsg", errMsg);
        return response;
    }


}
