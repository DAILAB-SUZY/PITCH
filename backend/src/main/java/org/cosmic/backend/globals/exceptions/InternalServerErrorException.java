package org.cosmic.backend.globals.exceptions;

public class InternalServerErrorException extends RuntimeException{
    protected static final String failedUploadFileError_Message="Exist AlbumLike Exception";

    public InternalServerErrorException(String errorMessage) {
        super(errorMessage);
    }

    public static String failedUploadFile()
    {
        return failedUploadFileError_Message;
    }
}
