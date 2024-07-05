package org.cosmic.backend.domain.playList.exceptions;

import org.cosmic.backend.globals.exceptions.UnAuthorizationException;

public class NotFoundUserException extends UnAuthorizationException {

    public NotFoundUserException(String errorMessage) {
        super(errorMessage);
    }
    public NotFoundUserException(){
        this("Not Found User Exception");
    }
}
