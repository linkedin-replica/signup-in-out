package userCommands;

import utils.JwtUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class SignOutCommand extends abstraction.Command {

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
        response.put("success", false);

        if(jwtToken != null && (Boolean)JwtUtils.validateToken(jwtToken).get("success"))
                response.put("success", true);

        return response;
    }
}