package org.cosmic.backend.globals.exceptions;

public class ConflictException extends RuntimeException{

    protected static String existAlbumLikeError_Message="Exist AlbumLike Exception";
    protected static String existCommentLikeError_Message="Exist CommentLike Exception";
    protected static String existBestAlbumError_Message="Exist BestAlbum Exception";

    public ConflictException(String errorMessage) {
        super(errorMessage);
    }

    public static String getExistAlbumLikeError()
    {
        return existAlbumLikeError_Message;
    }
    public static String getExistCommentLikeError()
    {
        return existCommentLikeError_Message;
    }
    public static String getExistBestAlbumError()
    {
        return existBestAlbumError_Message;
    }
}
