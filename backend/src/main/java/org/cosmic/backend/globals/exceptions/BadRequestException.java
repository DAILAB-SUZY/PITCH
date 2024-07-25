package org.cosmic.backend.globals.exceptions;

public class BadRequestException extends RuntimeException{

    protected static String notMatchAlbumChatError_Message="Not Match AlbumChat Exception";
    protected static String notMatchAlbumError_Message="Not Match Album Exception";
    protected static String notMatchBestAlbumError_Message="Not Match BestAlbum Exception";
    protected static String notMatchMusicDnaCountError_Message="Need 4 MusicDna";
    protected static String notMatchConditionError_Message="Not met password condition";
    protected static String notMatchPasswordError_Message="Not match password";

    public BadRequestException(String errorMessage) {
        super(errorMessage);
    }

    public static String getNotMatchConditionError()
    {
        return notMatchConditionError_Message;
    }
    public static String getNotMatchPasswordError()
    {
        return notMatchPasswordError_Message;
    }
    public static String getNotMatchMusicDnaCountError()
    {
        return notMatchMusicDnaCountError_Message;
    }
    public static String getNotMatchAlbumChatError()
    {
        return notMatchAlbumChatError_Message;
    }
    public static String getNotMatchAlbumError()
    {
        return notMatchAlbumError_Message;
    }
    public static String getNotMatchBestAlbumError()
    {
        return notMatchBestAlbumError_Message;
    }

}
