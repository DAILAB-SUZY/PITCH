package org.cosmic.backend.domain.user.exceptions;

public class NotExistEmailException extends RuntimeException  {
    public NotExistEmailException(String errorMessage) {
        super(errorMessage);
    }

    public NotExistEmailException(){
        this("Not found email");
    }
}
