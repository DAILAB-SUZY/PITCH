package org.cosmic.backend.domain.user.exceptions;

public class NotMatchPasswordException extends RuntimeException {
    public NotMatchPasswordException(String errorMessage) {
        super(errorMessage);
    }

    public NotMatchPasswordException(){
        this("Not match password");
    }
}
