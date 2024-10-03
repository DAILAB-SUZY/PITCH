package org.cosmic.backend.domain.bestAlbum.exceptions;

import org.cosmic.backend.globals.exceptions.BadRequestException;

public class NotMatchBestAlbumException extends BadRequestException {
    public NotMatchBestAlbumException(String errorMessage) {
        super(errorMessage);
    }
    public NotMatchBestAlbumException(){
        this(getNotMatchBestAlbumError());
    }
}
