package org.cosmic.backend.domain.File.exceptions;

import org.cosmic.backend.globals.exceptions.PayLoadTooLargeException;

public class ExceedFileSizeException extends PayLoadTooLargeException{
    public ExceedFileSizeException(String errorMessage) {
        super(errorMessage);
    }

    public ExceedFileSizeException(){
        this(getNotMatchFileSizeMessage());
    }
}
