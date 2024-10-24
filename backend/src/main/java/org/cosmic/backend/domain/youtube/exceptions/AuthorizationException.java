package org.cosmic.backend.domain.youtube.exceptions;

import org.cosmic.backend.globals.exceptions.NotFoundException;
import org.cosmic.backend.globals.exceptions.UnAuthorizationException;

public class AuthorizationException extends UnAuthorizationException {
    public AuthorizationException(String errorMessage) {
        super(errorMessage);
    }

    public AuthorizationException(){
        this(getNotAccessKeyError());
    }
}