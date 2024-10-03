package org.cosmic.backend.domain.post.exceptions;

import org.cosmic.backend.globals.exceptions.BadRequestException;
public class NotMatchUserException extends BadRequestException {
    public NotMatchUserException(String errorMessage) {
        super(errorMessage);
    }
    public NotMatchUserException(){
        this("Not Match User Exception");
    }
}
