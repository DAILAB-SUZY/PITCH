package org.cosmic.backend.domain.playList.exceptions;

import org.cosmic.backend.globals.exceptions.UnAuthorizationException;

public class NotFoundArtistException extends UnAuthorizationException {
    public NotFoundArtistException(String errorMessage) {
        super(errorMessage);
    }
    public NotFoundArtistException(){
        this("Not Found Artist Exception");
    }
}
