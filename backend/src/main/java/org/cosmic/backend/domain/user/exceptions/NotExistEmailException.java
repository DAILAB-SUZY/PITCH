package org.cosmic.backend.domain.user.exceptions;

import org.cosmic.backend.globals.exceptions.UnAuthorizationException;

public class NotExistEmailException extends UnAuthorizationException {
    public NotExistEmailException(String errorMessage) {
        super(errorMessage);
    }

    public NotExistEmailException(){
        this("Not found email");
    }
}
