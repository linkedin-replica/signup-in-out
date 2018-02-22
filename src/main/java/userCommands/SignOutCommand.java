package userCommands;

import java.util.HashMap;
import java.security.MessageDigest;
import java.util.LinkedHashMap;
import javax.xml.bind.DatatypeConverter;

public class SignOutCommand extends abstraction.Command {

    public SignOutCommand(HashMap<String, String> args) {
        super(args);
    }

    public LinkedHashMap<String, Object> execute() {
        return null; // wb7CMF
    }
}