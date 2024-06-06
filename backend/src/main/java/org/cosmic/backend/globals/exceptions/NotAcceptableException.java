package org.cosmic.backend.globals.exceptions;

public class NotAcceptableException extends RuntimeException{
    public NotAcceptableException(String errorMessage) {
        super(errorMessage);
    }
}
