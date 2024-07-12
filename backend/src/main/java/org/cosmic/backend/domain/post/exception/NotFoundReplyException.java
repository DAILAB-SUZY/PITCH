package org.cosmic.backend.domain.post.exception;

import org.cosmic.backend.globals.exceptions.NotFoundException;

public class NotFoundReplyException extends NotFoundException {
    public NotFoundReplyException(String errorMessage) {
        super(errorMessage);
    }
    public NotFoundReplyException(){
        this("Not found post");
    }
}