package org.cosmic.backend.domain.albumChat.exception;

import org.cosmic.backend.globals.exceptions.ConflictException;

public class ExistCommentLikeException extends ConflictException {
    public ExistCommentLikeException(String errorMessage) {
        super(errorMessage);
    }
    public ExistCommentLikeException(){
        this("Exist CommentLike Exception");
    }
}