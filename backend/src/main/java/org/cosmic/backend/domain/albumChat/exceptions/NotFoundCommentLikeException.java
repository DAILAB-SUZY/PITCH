package org.cosmic.backend.domain.albumChat.exceptions;

import org.cosmic.backend.globals.exceptions.NotFoundException;

public class NotFoundCommentLikeException extends NotFoundException {
    public NotFoundCommentLikeException(String errorMessage) {
        super(errorMessage);
    }
    public NotFoundCommentLikeException(){
        this("Not found CommentLike");
    }
}