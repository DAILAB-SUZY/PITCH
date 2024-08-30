package org.cosmic.backend.domain.bestAlbum.exceptions;

import org.cosmic.backend.globals.exceptions.BadRequestException;

public class NotMatchAlbumException extends BadRequestException {
    public NotMatchAlbumException(String errorMessage) {
        super(errorMessage);
    }
    public NotMatchAlbumException(){
        this(getNotMatchAlbumError());
    }
}
