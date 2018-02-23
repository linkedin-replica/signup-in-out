package userCommands;

import database.ArangoHandler;
import database.DatabaseHandler;
import database.MysqlHandler;
import model.User;
import model.UserProfile;
import modules.SHA512;

import org.apache.commons.validator.routines.EmailValidator;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class SignUpCommand extends abstraction.Command {

    private DatabaseHandler sqldb;
    private DatabaseHandler nosqldb;
    public SignUpCommand(HashMap<String, String> args) throws IOException{
        super(args);
        sqldb = new MysqlHandler();
        nosqldb = new ArangoHandler();
    }

    /**
     * Validates user profile
     */
    public boolean isValidUserEmail(String email) {
        // Check if valid email
        return EmailValidator.getInstance().isValid(email);
    }


    public LinkedHashMap<String, Object> execute() {
        LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();

        if(args.containsKey("email") && args.containsKey("password") && args.containsKey("firstName") && args.containsKey("lastName")) {

            String email = args.get("email");
            String password = SHA512.hash(args.get("password"));

            sqldb.connect();
            User user = sqldb.getUser(email);
            sqldb.disconnect();

            if(user != null){
                response.put("error", "This user already exists, Do you want to sign in?");
            } else if(isValidUserEmail(email)){
                response.put("error", "Invalid Email");
            } else{
                String key = sqldb.createUser(email, password);
                String firstName = args.get("firstName");
                String lastName = args.get("lastName");

                UserProfile userProfile = new UserProfile(email, firstName, lastName);
                String profileKey = nosqldb.createUser(userProfile);
            }
        }else
            response.put("error", "Missing information");

        return response;
    }
}