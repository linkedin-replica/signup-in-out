package com.linkedin.replica.signing.exceptions;

/**
 * Throw exception concerning signing service:
 * - Incorrect user name or password
 * - User already exist
 */
public class SigningException extends RuntimeException{
    public SigningException(String message){
        super(message);
    }
}
