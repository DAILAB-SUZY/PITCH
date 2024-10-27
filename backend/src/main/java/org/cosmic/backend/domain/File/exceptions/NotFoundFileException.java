package org.cosmic.backend.domain.File.exceptions;

import org.cosmic.backend.globals.exceptions.NotFoundException;

public class NotFoundFileException extends NotFoundException {
    public NotFoundFileException(String errorMessage) {
        super(errorMessage);
    }
    public NotFoundFileException(){
        this(getNotFoundFileError());
    }
}
