package org.cosmic.backend.domain.youtube.exceptions;

import org.cosmic.backend.globals.exceptions.JsonProcessingException;

public class NotProcessApiException extends JsonProcessingException {
    public NotProcessApiException(String errorMessage) {
        super(errorMessage);
    }

    public NotProcessApiException(){
        this(presentProblem());
    }
}
