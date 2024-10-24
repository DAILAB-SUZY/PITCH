package org.cosmic.backend.globals.exceptions;

public class UnAuthorizationException extends RuntimeException{

    protected static final String notExistYoutubeAccessKey="don't give youtube accesstoken";
    protected static final String notMatchYoutubeAccessKey="not match user accessKey";

    public UnAuthorizationException(String errorMessage) {
        super(errorMessage);
    }
    public static String getNotAccessKeyError()
    {
        return notExistYoutubeAccessKey;
    }
    public static String NotMatchAccessKeyError()
    {
        return notMatchYoutubeAccessKey;
    }
}
