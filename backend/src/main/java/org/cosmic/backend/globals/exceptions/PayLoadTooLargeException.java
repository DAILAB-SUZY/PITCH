package org.cosmic.backend.globals.exceptions;

public class PayLoadTooLargeException extends RuntimeException {
    protected static final String NOT_MATCH_FILE_SIZE_MESSAGE="exceed filesize";
    public PayLoadTooLargeException(String errorMessage) {
        super(errorMessage);
    }
    public static String getNotMatchFileSizeMessage()
    {
        return NOT_MATCH_FILE_SIZE_MESSAGE;
    }
}
