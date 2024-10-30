package org.cosmic.backend.domain.favoriteArtist.exceptions;

import org.cosmic.backend.globals.exceptions.BadRequestException;

public class IdNullException extends BadRequestException {
    public IdNullException(String errorMessage) {
        super(errorMessage);
    }
    public IdNullException(){
        this(getNullIncludeElementException());
    }
}
