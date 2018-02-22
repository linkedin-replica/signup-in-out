package userCommands;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

import java.io.UnsupportedEncodingException;
import java.security.SignatureException;
import java.util.HashMap;
import java.security.MessageDigest;
import java.util.LinkedHashMap;
import javax.xml.bind.DatatypeConverter;

public class SignOutCommand extends abstraction.Command {

    private static final  String secretKey = "NpapHelGNRvAWEc0XGLYDJI83rdo5yJp1sxAS";

    public SignOutCommand(HashMap<String, String> args) {
        super(args);
    }

    public LinkedHashMap<String, Object> execute() throws UnsupportedEncodingException {

        LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
        String jwtToken = args.get("jwtToken");

        if(jwtToken != null) {
            try {
                //OK, we can trust this JWT
                Jws<Claims> claims = Jwts.parser()
                        .setSigningKey(secretKey.getBytes("UTF-8"))
                        .parseClaimsJws(jwtToken);
                System.out.println("Succcess");
            } catch (io.jsonwebtoken.SignatureException e) {
                System.out.println(e.getMessage());
                //don't trust the JWT!
            } catch (io.jsonwebtoken.ExpiredJwtException e){
                System.out.println(e.getMessage());
            }
            catch (Exception e) {

            }
        }
        else {

        }

        return response;
    }
}