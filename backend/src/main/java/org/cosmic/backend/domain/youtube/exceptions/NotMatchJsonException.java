package org.cosmic.backend.domain.youtube.exceptions;

import org.cosmic.backend.globals.exceptions.ForbiddenException;
import org.cosmic.backend.globals.exceptions.JsonMappingException;

public class NotMatchJsonException extends JsonMappingException {
    public NotMatchJsonException(String errorMessage) {
        super(errorMessage);
    }

    public NotMatchJsonException(){
        this(notMatchJson());
    }
}