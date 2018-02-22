package userCommands;

import database.DatabaseHandler;
import database.MysqlHandler;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;

import java.io.IOException;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;

public class SignInCommand extends abstraction.Command {

    private static final  String secretKey = "NpapHelGNRvAWEc0XGLYDJI83rdo5yJp1sxAS";
    private DatabaseHandler db;


    public SignInCommand(HashMap<String, String> args) throws IOException {
        super(args);
        db = new MysqlHandler();
    }

    public String execute() {
        return null;
    }

}
