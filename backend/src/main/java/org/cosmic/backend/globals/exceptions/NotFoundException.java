package org.cosmic.backend.globals.exceptions;

public class NotFoundException extends RuntimeException{

    protected static String notFoundAlbumChatCommentError_Message="Not found albumChatComment";
    protected static String notFoundAlbumChatCommentLikeError_Message="Not found albumChatComment";
    protected static String notFoundAlbumChatError_Message="Not found albumChat page";
    protected static String notFoundEmotionError_Message="Not Found Emotion";
    protected static String notExistEmailError_Message="\"Not found email";

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
