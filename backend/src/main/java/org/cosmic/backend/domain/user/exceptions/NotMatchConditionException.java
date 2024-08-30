package org.cosmic.backend.domain.user.exceptions;

import org.cosmic.backend.globals.exceptions.BadRequestException;

public class NotMatchConditionException extends BadRequestException {
    public NotMatchConditionException(String errorMessage) {
        super(errorMessage);
    }

    public NotMatchConditionException(){
        this(getNotMatchConditionError());
    }
}