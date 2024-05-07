package org.cosmic.backend.domain.user.exceptions;

public class NotMatchConditionException extends RuntimeException {
    public NotMatchConditionException(String errorMessage) {
        super(errorMessage);
    }

    public NotMatchConditionException(){
        this("Not met password condition");
    }
}