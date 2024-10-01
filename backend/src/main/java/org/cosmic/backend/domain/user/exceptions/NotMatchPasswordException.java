package org.cosmic.backend.domain.user.exceptions;

import org.cosmic.backend.globals.exceptions.BadRequestException;

public class NotMatchPasswordException extends BadRequestException {
    public NotMatchPasswordException(String errorMessage) {
        super(errorMessage);
    }

    public NotMatchPasswordException(){
        this(getNotMatchPasswordError());
    }
}
