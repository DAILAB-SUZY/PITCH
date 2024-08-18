package org.cosmic.backend.globals.exceptions;

public class NotFoundException extends RuntimeException{

    protected static final String notFoundAlbumChatCommentError_Message="Not found albumChatComment";
    protected static final String notFoundAlbumChatCommentLikeError_Message="Not found albumChatComment";
    protected static final String notFoundAlbumChatError_Message="Not found albumChat page";
    protected static final String notFoundEmotionError_Message="Not Found Emotion";
    protected static final String notExistEmailError_Message="\"Not found email";

    public NotFoundException(String errorMessage) {
        super(errorMessage);
    }
    public static String getNotExistEmailError()
    {
        return notExistEmailError_Message;
    }
    public static String getNotFoundAlbumChatCommentError()
    {
        return notFoundAlbumChatCommentError_Message;
    }
    public static String getNotFoundAlbumChatCommentLikeError()
    {
        return notFoundAlbumChatCommentLikeError_Message;
    }
    public static String getNotFoundAlbumChatError()
    {
        return notFoundAlbumChatError_Message;
    }
    public static String getNotFoundEmotionError()
    {
        return notFoundEmotionError_Message;
    }
}
