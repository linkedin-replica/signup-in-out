package abstraction;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedHashMap;

public abstract class Command {
    protected HashMap<String, String> args;

    public Command(HashMap<String, String> args) {
        this.args = args;
    }

    /**
     * Execute the command
     *
     * @return The output (if any) of the command
     */
    public abstract LinkedHashMap<String, Object> execute() throws NoSuchAlgorithmException, UnsupportedEncodingException;
}
