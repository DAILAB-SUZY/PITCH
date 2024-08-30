package org.cosmic.backend.domain.post.exceptions;

import org.cosmic.backend.globals.exceptions.BadRequestException;

public class NotMatchPostException extends BadRequestException {
    public NotMatchPostException(String errorMessage) {
        super(errorMessage);
    }
    public NotMatchPostException(){
        this("Not Match Post Exception");
    }
}