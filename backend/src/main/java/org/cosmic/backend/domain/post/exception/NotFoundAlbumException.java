package org.cosmic.backend.domain.post.exception;

import org.cosmic.backend.globals.exceptions.NotFoundException;

public class NotFoundAlbumException extends NotFoundException {
    public NotFoundAlbumException(String errorMessage) {
        super(errorMessage);
    }
    public NotFoundAlbumException(){
        this("Not found album");
    }
}
