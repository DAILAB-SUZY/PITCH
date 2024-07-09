package org.cosmic.backend.globals.exceptions;

public class ConflictException extends RuntimeException{
    public ConflictException(String errorMessage) {
        super(errorMessage);
    }
}
