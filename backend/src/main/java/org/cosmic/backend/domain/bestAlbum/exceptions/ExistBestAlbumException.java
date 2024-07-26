package org.cosmic.backend.domain.bestAlbum.exceptions;

import org.cosmic.backend.globals.exceptions.ConflictException;

public class ExistBestAlbumException  extends ConflictException {
    public ExistBestAlbumException(String errorMessage) {
        super(errorMessage);
    }
    public ExistBestAlbumException(){
        this(ConflictException.getExistBestAlbumError());
    }
}