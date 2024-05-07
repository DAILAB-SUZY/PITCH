package org.cosmic.backend.domain.mail.exceptions;

public class ExistEmailException extends RuntimeException {
    public ExistEmailException(String errorMessage) {
        super(errorMessage);
    }

    public ExistEmailException(){
        this("Already exist email");
    }
}
