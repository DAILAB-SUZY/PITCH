package org.cosmic.backend.globals.exceptions;

public class UnAuthorizationException extends RuntimeException{
    public UnAuthorizationException(String errorMessage) {
        super(errorMessage);
    }
}
