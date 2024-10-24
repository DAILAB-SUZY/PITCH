package org.cosmic.backend.globals.exceptions;


public class ForbiddenException extends RuntimeException{

    protected static final String notAccessException="Not Enter Page";
    protected static final String notMatchJson="Not Match Json";

    public ForbiddenException(String errorMessage) {
        super(errorMessage);
    }

    public static String notAccessPageError()
    {
        return notAccessException;
    }
    public static String notMatchJson()
    {
        return notMatchJson;
    }
}