package org.cosmic.backend.domain.File.exceptions;

import org.cosmic.backend.globals.exceptions.InternalServerErrorException;

public class NotReadableException extends InternalServerErrorException {
    public NotReadableException(String errorMessage) {
        super(errorMessage);
    }
    public NotReadableException(){
        this(failedReadFile());
    }
}
