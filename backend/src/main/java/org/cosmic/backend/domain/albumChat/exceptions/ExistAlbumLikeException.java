package org.cosmic.backend.domain.albumChat.exceptions;

import org.cosmic.backend.globals.exceptions.ConflictException;

public class ExistAlbumLikeException extends ConflictException {
    public ExistAlbumLikeException(String errorMessage) {
        super(errorMessage);
    }
    public ExistAlbumLikeException(){
        this(ConflictException.getExistAlbumLikeError());
    }
}