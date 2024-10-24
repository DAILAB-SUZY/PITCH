package org.cosmic.backend.globals.exceptions;

public class UnAuthorizationException extends RuntimeException{

    protected static final String notExistYoutubeAccessKey="don't give youtube accesstoken";

    public UnAuthorizationException(String errorMessage) {
        super(errorMessage);
    }
    public static String getNotAccessKeyError()
    {
        return notExistYoutubeAccessKey;
    }
}
