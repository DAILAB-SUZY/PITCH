package org.cosmic.backend.domain.post.exceptions;

import org.cosmic.backend.globals.exceptions.NotFoundException;

public class NotFoundPostException extends NotFoundException {
    public NotFoundPostException(String errorMessage) {
        super(errorMessage);
    }
    public NotFoundPostException(){
        this("Not found post");
    }
}