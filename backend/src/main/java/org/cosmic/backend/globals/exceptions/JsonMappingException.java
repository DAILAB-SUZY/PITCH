package org.cosmic.backend.globals.exceptions;

public class JsonMappingException extends RuntimeException {

    protected static final String notMatchJson="Not Match Json";
    public JsonMappingException(String errorMessage) {
        super(errorMessage);
    }
    public static String notMatchJson()
    {
        return notMatchJson;
    }
}
