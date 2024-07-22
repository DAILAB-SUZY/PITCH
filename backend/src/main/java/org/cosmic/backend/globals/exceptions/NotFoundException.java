package org.cosmic.backend.globals.exceptions;

public class NotFoundException extends RuntimeException{

    protected static String notFoundAlbumChatCommentError_Message="Not found albumChatComment";

    public NotFoundException(String errorMessage) {
        super(errorMessage);
    }

    public static String getNotFoundAlbumChatCommentError()
    {
        return notFoundAlbumChatCommentError_Message;
    }
}
