package org.cosmic.backend.globals.exceptions;

public class UnSupportedMediaTypeException extends RuntimeException {

    protected static final String NOT_MATCH_FILE_FORMAT_MESSAGE ="Not match file format Exception";
    public UnSupportedMediaTypeException(String errorMessage) {
        super(errorMessage);
    }
    public static String getNotMatchFileFormatError()
    {
        return NOT_MATCH_FILE_FORMAT_MESSAGE;
    }
}
