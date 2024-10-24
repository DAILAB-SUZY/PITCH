package org.cosmic.backend.globals.exceptions;

public class JsonProcessingException extends RuntimeException {

    protected static final String presentProcessingProblem="present processing problem";

    public JsonProcessingException(String errorMessage) {super(errorMessage);}

    public static String presentProblem()
    {
        return presentProcessingProblem;
    }

}
