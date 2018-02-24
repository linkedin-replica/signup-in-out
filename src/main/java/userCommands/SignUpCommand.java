package userCommands;

import database.ArangoHandler;
import database.DatabaseHandler;
import database.MysqlHandler;
import model.User;
import model.UserProfile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.SHA512;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class SignUpCommand extends abstraction.Command {

    private DatabaseHandler sqldbHandler;
    private DatabaseHandler nosqldbHandler;

    private static final Logger LOGGER = LogManager.getLogger(SignUpCommand.class.getName());

    public SignUpCommand(HashMap<String, String> args){
        super(args);
        sqldbHandler = new MysqlHandler();
        nosqldbHandler = new ArangoHandler();
    }

    /**
     * Validates user profile
     */
    public boolean isValidUserEmail(String email) {
        // Check if valid email
        return EmailValidator.getInstance().isValid(email);
    }

    /**
     * Handle the sign up process and generate the ID of the inserted user to be used accross services
     * @return Response with result and error message if an error occurs
     */
    public LinkedHashMap<String, Object> execute() {
        LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
        String errMsg= "Nothing is wrong";
        Boolean success = false;
        String profileKey;


        if(args.containsKey("email") && args.containsKey("password") && args.containsKey("firstName") && args.containsKey("lastName")) {

            String email = args.get("email");
            String password = SHA512.hash(args.get("password"));

            sqldbHandler.connect();
            User user = (User) sqldbHandler.getUser(email);

            if(user != null){
                errMsg = "This user already exists, Do you want to sign in?";
            } else if(isValidUserEmail(email)){
                LOGGER.warn("Invalid email");
                errMsg =  "Invalid Email";
            } else{
                User newUser= new User();
                newUser.set(email, email);
                newUser.set(password, password);
                String key = sqldbHandler.createUser(newUser); // use this key as the id of Userprofile object

                nosqldbHandler.connect();
                String firstName = args.get("firstName");
                String lastName = args.get("lastName");

                UserProfile userProfile = UserProfile.Instantiate();
                userProfile.setId(key);
                userProfile.setEmail(email);
                userProfile.setFirstName(firstName);
                userProfile.setLastName(lastName);

                profileKey = nosqldbHandler.createUser(userProfile); // use this profile key to communicate with arango(get/ delete)
                nosqldbHandler.disconnect();
                success = true;
                response.put("profileKey", profileKey);
            }
        }else
            errMsg = "Missing information";

        sqldbHandler.disconnect();
        response.put("success",success);
        response.put("error", errMsg);
        return response;
    }
}