package org.cosmic.backend.domain.post.exceptions;

import org.cosmic.backend.globals.exceptions.ConflictException;

public class ExistLikeException extends ConflictException {
    public ExistLikeException(String errorMessage) {
        super(errorMessage);
    }
    public ExistLikeException(){
        this("Exist Like Exception");
    }
}