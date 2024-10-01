package org.cosmic.backend.domain.post.exceptions;

import org.cosmic.backend.globals.exceptions.NotFoundException;

public class NotFoundLikeException extends NotFoundException {
    public NotFoundLikeException(String errorMessage) {
        super(errorMessage);
    }
    public NotFoundLikeException(){
        this("Not found like");
    }
}