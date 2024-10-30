package org.cosmic.backend.domain.File.exceptions;

import org.cosmic.backend.globals.exceptions.BadRequestException;
import org.cosmic.backend.globals.exceptions.InternalServerErrorException;

public class NotInputFileException extends BadRequestException {
    public NotInputFileException(String errorMessage) {
        super(errorMessage);
    }
    public NotInputFileException(){
        this(getNotInputFileError());
    }
}
