package org.cosmic.backend.domain.youtube.exceptions;

import org.cosmic.backend.globals.exceptions.JsonMappingException;
import org.cosmic.backend.globals.exceptions.UnAuthorizationException;

public class NotMatchKeyException extends UnAuthorizationException {
    public NotMatchKeyException(String errorMessage) {
        super(errorMessage);
    }

    public NotMatchKeyException(){
        this(NotMatchAccessKeyError());
    }
}