package org.cosmic.backend.globals.exceptions;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
