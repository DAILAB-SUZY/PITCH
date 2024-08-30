package org.cosmic.backend.domain.post.exceptions;

import org.cosmic.backend.globals.exceptions.NotFoundException;

public class NotFoundCommentException extends NotFoundException {
    public NotFoundCommentException(String errorMessage) {
        super(errorMessage);
    }
    public NotFoundCommentException(){
        this("Not found comment");
    }
}
