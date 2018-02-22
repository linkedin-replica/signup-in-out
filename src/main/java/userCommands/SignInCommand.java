package userCommands;

import database.DatabaseHandler;
import database.MysqlHandler;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import model.User;
import modules.SHA512;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class SignInCommand extends abstraction.Command {

    private static final  String secretKey = "NpapHelGNRvAWEc0XGLYDJI83rdo5yJp1sxAS";
    private DatabaseHandler db;


    public SignInCommand(HashMap<String, String> args) throws IOException {
        super(args);
        db = new MysqlHandler();
    }

    /**
     * Handle the sign in process and generate the JWT token to be used in authontication
     * //TODO:: handle Try and catch 3shan ana mbdon mn throws,
     * //TODO:: add the token to the cache server
     * @return Response with result and error if exist
     * @throws NoSuchAlgorithmException source SHA512 hash class
     * @throws UnsupportedEncodingException
     */
    public LinkedHashMap<String, Object> execute() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();

        if(args.containsKey("email") && args.containsKey("password")){

            String email = args.get("email");
            String password = SHA512.hash(args.get("password"));

            db.connect();
            User user = db.getUser(email);
            db.disconnect();

            if(user != null){
                if(user.get("password").equals(password)){

                    String jwt = Jwts.builder()
                            .setSubject("users/TzMUocMF4p")
                            .setExpiration(new Date())
                            .claim("email", args.get("email"))
                            .claim("scope", "self groups/admins")
                            .signWith(
                                    SignatureAlgorithm.HS256,
                                    secretKey.getBytes("UTF-8")
                            ).compact();

                    response.put("results", jwt);
                }else
                    response.put("error", "Incorrect password");
            }else
                response.put("error", "No such user");
        }else
            response.put("error", "Missing information");

        return response;
    }
}
