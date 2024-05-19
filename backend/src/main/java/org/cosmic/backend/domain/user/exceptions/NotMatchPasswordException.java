package org.cosmic.backend.domain.user.exceptions;

import org.cosmic.backend.globals.exceptions.NotFoundException;

public class NotMatchPasswordException extends NotFoundException {
    public NotMatchPasswordException(String errorMessage) {
        super(errorMessage);
    }

    public NotMatchPasswordException(){
        this("Not match password");
    }
}
