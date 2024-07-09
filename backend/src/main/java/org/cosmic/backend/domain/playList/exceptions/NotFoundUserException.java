package org.cosmic.backend.domain.playList.exceptions;

import org.cosmic.backend.globals.exceptions.NotFoundException;

public class NotFoundUserException extends NotFoundException {

    public NotFoundUserException(String errorMessage) {
        super(errorMessage);
    }
    public NotFoundUserException(){
        this("Not Found User Exception");
    }
}
