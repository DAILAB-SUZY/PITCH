package org.cosmic.backend.domain.auth.exceptions;

public class CredentialNotMatchException extends RuntimeException{
    public CredentialNotMatchException(){
        super("Email or Password is wrong");
    }
}
