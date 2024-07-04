package org.cosmic.backend.domain.bestAlbum.exception;

import org.cosmic.backend.globals.exceptions.UnAuthorizationException;

public class NotMatchAlbumException extends UnAuthorizationException {
    public NotMatchAlbumException(String errorMessage) {
        super(errorMessage);
    }
    public NotMatchAlbumException(){
        this("Not Match Album Exception");
    }
}
