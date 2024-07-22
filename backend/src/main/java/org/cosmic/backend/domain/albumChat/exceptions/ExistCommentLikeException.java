package org.cosmic.backend.domain.albumChat.exceptions;

import org.cosmic.backend.globals.exceptions.ConflictException;

public class ExistCommentLikeException extends ConflictException {
    public ExistCommentLikeException(String errorMessage) {
        super(errorMessage);
    }
    public ExistCommentLikeException(){
        this(ConflictException.getExistCommentLikeError());
    }
}