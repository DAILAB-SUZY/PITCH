package org.cosmic.backend.domain.albumChat.exception;

import org.cosmic.backend.globals.exceptions.NotFoundException;

public class NotFoundCommentLikeException extends NotFoundException {
    public NotFoundCommentLikeException(String errorMessage) {
        super(errorMessage);
    }
    public NotFoundCommentLikeException(){
        this("Not found CommentLike");
    }
}