package com.linkedin.replica.signing.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultJws;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

// TODO: Discuss the way of signing the token and which key to be used
// TODO: Discuss if we should refresh the token

public class JwtUtils {

    private static final Logger LOGGER = LogManager.getLogger(JwtUtils.class.getName());
    private static final String secretKey = "NpapHelGNRvAWEc0XGL0ESMY1MAWGOOD2FL3NOS4ANA5ASHR" +
            "AF6WANA7MABDOON9YA10TEFA11YDJI83rdo5yJp1sxAS";

    /**
     * Generate jwt token based on claims, id of user to be authenticated and expiration duration
     *
     * @param claims      key-value pair to be put in the token body
     * @param userId      id of user in mysql com.linkedin.replica.signing.database
     * @param secretKey   key to be for signing the token
     * @param minToExpire time in minutes this token will be valid
     * @return jwt token for user
     */

    public static String generateToken(Map<String, Object> claims, String userId, String secretKey, long minToExpire) throws UnsupportedEncodingException {
        String token = null;
        token = Jwts.builder()
                .setClaims(claims)
                .setId(userId)
                .setIssuedAt(new Date())
                .setIssuer("linkedin.login")
                .setExpiration(generateExpirationDate(minToExpire))
                .signWith(SignatureAlgorithm.HS512, secretKey.getBytes("UTF-8"))
                .compact();
        return token;
    }

    /**
     * Generate jwt token based on claims, id of user to be authenticated and expiration duration
     *
     * @param claims      key-value pair to be put in the token body
     * @param userId      id of user in mysql com.linkedin.replica.signing.database
     * @param minToExpire time in minutes this token will be valid
     * @return map holding token, error message and success boolean
     */

    public static String generateToken(Map<String, Object> claims, String userId, long minToExpire) throws UnsupportedEncodingException {
        return generateToken(claims, userId, secretKey, minToExpire);
    }


    /**
     * Get claims (stored data) from a valid token
     * @param token
     * @return
     * @throws UnsupportedEncodingException
     */
    public static Jws<Claims> getClaims(String token) throws UnsupportedEncodingException {
        return Jwts.parser()
                .setSigningKey(secretKey.getBytes("UTF-8"))
                .parseClaimsJws(token);
    }


    /**
     * Validate jwt token
     *
     * @param token jwt token to be authenticated
     * @return Weather
     */

    public static boolean validateToken(String token) {

        try {
            getClaims(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private static Date generateExpirationDate(long minutes) {
        return new Date(System.currentTimeMillis() + minutes * 60 * 1000);
    }
}