package org.cosmic.backend.domain.File.exceptions;

import org.cosmic.backend.globals.exceptions.UnSupportedMediaTypeException;

public class NotMatchFileFormatException extends UnSupportedMediaTypeException {
    public NotMatchFileFormatException(String errorMessage) {
        super(errorMessage);
    }
    public NotMatchFileFormatException(){
        this(getNotMatchFileFormatError());
    }
}
