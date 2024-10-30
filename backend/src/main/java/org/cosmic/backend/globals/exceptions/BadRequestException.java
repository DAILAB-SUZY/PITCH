package org.cosmic.backend.globals.exceptions;

public class BadRequestException extends RuntimeException{

    protected static final String NOT_MATCH_ALBUM_CHAT_EXCEPTION ="Not Match AlbumChat Exception";
    protected static final String NOT_MATCH_ALBUM_EXCEPTION ="Not Match Album Exception";
    protected static final String NOT_MATCH_BEST_ALBUM_EXCEPTION ="Not Match BestAlbum Exception";
    protected static final String NOT_MATCH_MUSIC_DNA_COUNT_ERROR_MESSAGE ="Need 4 MusicDna";
    protected static final String NOT_MATCH_CONDITION_ERROR_MESSAGE ="Not met password condition";
    protected static final String NOT_MATCH_PASSWORD_ERROR_MESSAGE ="Not match password";
    protected static final String NULL_INCLUDE_ELEMENT_EXCEPTION="Include Null Element";

    public BadRequestException(String errorMessage) {
        super(errorMessage);
    }

    public static String getNotMatchConditionError()
    {
        return NOT_MATCH_CONDITION_ERROR_MESSAGE;
    }
    public static String getNotMatchPasswordError()
    {
        return NOT_MATCH_PASSWORD_ERROR_MESSAGE;
    }
    public static String getNotMatchMusicDnaCountError()
    {
        return NOT_MATCH_MUSIC_DNA_COUNT_ERROR_MESSAGE;
    }
    public static String getNotMatchAlbumChatError()
    {
        return NOT_MATCH_ALBUM_CHAT_EXCEPTION;
    }
    public static String getNotMatchAlbumError()
    {
        return NOT_MATCH_ALBUM_EXCEPTION;
    }
    public static String getNotMatchBestAlbumError()
    {
        return NOT_MATCH_BEST_ALBUM_EXCEPTION;
    }
    public static String getNullIncludeElementException()
    {
        return NULL_INCLUDE_ELEMENT_EXCEPTION;
    }
}
