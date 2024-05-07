package org.cosmic.backend.domain.user.exceptions;

public class NullException extends RuntimeException {

    public NullException(String errorMessage) {
        super(errorMessage);
    }

    public NullException(){
        this("Not given data");
    }
}
