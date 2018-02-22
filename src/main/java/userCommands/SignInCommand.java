package userCommands;

import database.DatabaseHandler;
import database.MysqlHandler;
import io.jsonwebtoken.JwtBuilder;
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
     * Create the JWT token for a user which holds his email and scope
     * @param id User id
     * @param subject of the token
     * @param issuer Login service
     * @param email Email of the user
     * @param scope The fields that this user can access
     * @param expOffset Number defines the offset of Milliseconds
     * @return Token string
     * @throws UnsupportedEncodingException
     */
    public String createJWT(String id, String subject, String issuer, String email, String scope, long expOffset) throws UnsupportedEncodingException {

        long currTime = System.currentTimeMillis();
        JwtBuilder builder = Jwts.builder().setId(id)
                .setSubject(subject)
                .setIssuer(issuer)
                .setIssuedAt(new Date(currTime))
                .setExpiration(new Date(currTime + expOffset))
                .claim("email", email)
                .claim("scope", scope)
                .signWith(
                        SignatureAlgorithm.HS512,
                        secretKey.getBytes("UTF-8"));

        return builder.compact();
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
                if(user.get("password").equals(password))
                    response.put("results", createJWT(user.getIdName(), "user", "login", email, "self groups/admins", 1000000));
                else
                    response.put("error", "Incorrect password");
            }else
                response.put("error", "No such user");
        }else
            response.put("error", "Missing information");

        return response;
    }


}
