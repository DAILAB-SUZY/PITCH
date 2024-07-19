package org.cosmic.backend.domain.post.exception;

import org.cosmic.backend.globals.exceptions.BadRequestException;

public class NotMatchCommentException extends BadRequestException {
    public NotMatchCommentException(String errorMessage) {
        super(errorMessage);
    }
    public NotMatchCommentException(){
        this("Not Match Comment Exception");
    }
}