package org.cosmic.backend.domain.playList.exceptions;

import org.cosmic.backend.globals.exceptions.NotFoundException;

public class NotFoundArtistException extends NotFoundException {
    public NotFoundArtistException(String errorMessage) {
        super(errorMessage);
    }
    public NotFoundArtistException(){
        this("Not Found Artist Exception");
    }
}
