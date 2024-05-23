package org.cosmic.backend.domain.mail.exceptions;

import org.cosmic.backend.globals.exceptions.UnAuthorizationException;

public class ExistEmailException extends UnAuthorizationException {
    public ExistEmailException(String errorMessage) {
        super(errorMessage);
    }

    public ExistEmailException(){
        this("Already exist email");
    }
}
