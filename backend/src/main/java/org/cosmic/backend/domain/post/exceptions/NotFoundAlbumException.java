package org.cosmic.backend.domain.post.exceptions;

import org.cosmic.backend.globals.exceptions.NotFoundException;

public class NotFoundAlbumException extends NotFoundException {
    public NotFoundAlbumException(String errorMessage) {
        super(errorMessage);
    }
    public NotFoundAlbumException(){
        this("Not found album");
    }
}
