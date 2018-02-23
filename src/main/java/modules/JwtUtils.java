package modules;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class JwtUtils {

    private static final Logger LOGGER = LogManager.getLogger(JwtUtils.class.getName());
    private static final  String secretKey = "NpapHelGNRvAWEc0XGL0ESMY1MAWGOOD2FL3NOS4ANA5ASHR" +
            "AF6WANA7MABDOON9YA10TEFA11YDJI83rdo5yJp1sxAS";

    /**
     * Generate jwt token based on claims, id of user to be authenticated and expiration duration
     * @param claims key-value pair to be put in the token body
     * @param userId id of user in mysql database
     * @param minToExpire time in minutes this token will be valid
     * @return map holding token, error message and success boolean
     */

    public Map<String, Object> generateToken(Map<String, Object> claims, String userId, long minToExpire) {
        return generateToken(claims, userId, secretKey, minToExpire);
    }

    public Map<String, Object> generateToken(Map<String, Object> claims, String userId, String secretKey, long minToExpire) {
        String token = null;
        String errMsg = "No errors";
        Boolean success = false;
        Map<String, Object> response = new LinkedHashMap<String, Object>();
        try {
            token =  Jwts.builder()
                    .setId(userId)
                    .setIssuedAt(new Date())
                    .setIssuer("linkedin.login")
                    .setClaims(claims)
                    .setExpiration(this.generateExpirationDate(minToExpire))
                    .signWith(SignatureAlgorithm.HS512, secretKey.getBytes("UTF-8"))
                    .compact();
        } catch (Exception ex) {
            LOGGER.warn(ex.getMessage());
            errMsg = "Something went wrong :(";
            success = false;
        }
        finally {
            response.put("token", token);
            response.put("errMsg", errMsg);
            response.put("success", success);
            return response;
        }
    }

    /**
     * Validate jwt token and return claims in its body
     * @param token jwt token to be authenticated
     * @param secretKey key used in signing the token
     * @return map holding claims, error message and success boolean
     */

    public Map<String, Object> validateToken(String token, String secretKey) {
        Jws<Claims> claims = null;
        String errMsg = "No errors";
        Boolean success = false;
        Map<String, Object> response = new LinkedHashMap<String, Object>();
        try {
            claims = Jwts.parser()
                    .setSigningKey(secretKey.getBytes("UTF-8"))
                    .parseClaimsJws(token);
            success = true;
        }
        catch (io.jsonwebtoken.SignatureException ex) {
            //don't trust the JWT!
            LOGGER.warn(ex.getMessage());
            errMsg = "Failed to verify token";
            success = false;
        } catch (io.jsonwebtoken.ExpiredJwtException ex){
            LOGGER.warn(ex.getMessage());
            errMsg = "This token has expired";
            success = false;
        }
        catch (Exception ex)
        {
            LOGGER.warn(ex.getMessage());
            LOGGER.warn("Failed to authenticate the jwt token");
            errMsg = "Something went wrong :(";
            success = false;
        }
        finally {
            response.put("claims", claims);
            response.put("errMsg", errMsg);
            response.put("success", success);
            return response;
        }
    }

    public Map<String, Object> validateToken(String token) {
        return validateToken(token, secretKey);
    }

    /**
     * Return date of expiration for token generation
     * @param minutes till it expires
     * @return date of token expiration
     */

    private Date generateExpirationDate(long minutes) {
        return new Date(System.currentTimeMillis() + minutes * 60 * 1000);
    }
}
