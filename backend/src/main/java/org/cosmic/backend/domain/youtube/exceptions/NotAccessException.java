package org.cosmic.backend.domain.youtube.exceptions;

import org.cosmic.backend.globals.exceptions.ForbiddenException;

public class NotAccessException extends ForbiddenException {
    public NotAccessException(String errorMessage) {
        super(errorMessage);
    }

    public NotAccessException(){
        this(notAccessPageError());
    }
}