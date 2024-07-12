package org.cosmic.backend.domain.albumChat.exception;

import org.cosmic.backend.globals.exceptions.ConflictException;

public class ExistAlbumLikeException extends ConflictException {
    public ExistAlbumLikeException(String errorMessage) {
        super(errorMessage);
    }
    public ExistAlbumLikeException(){
        this("Exist AlbumLike Exception");
    }
}