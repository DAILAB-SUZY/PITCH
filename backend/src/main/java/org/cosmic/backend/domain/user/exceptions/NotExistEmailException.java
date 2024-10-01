package org.cosmic.backend.domain.user.exceptions;

import org.cosmic.backend.globals.exceptions.NotFoundException;

public class NotExistEmailException extends NotFoundException {
    public NotExistEmailException(String errorMessage) {
        super(errorMessage);
    }
    public NotExistEmailException(){
        this(getNotExistEmailError());
    }
}
