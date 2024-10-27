package org.cosmic.backend.globals.exceptions;

public class InternalServerErrorException extends RuntimeException{
    protected static final String failedUploadFileError_Message="Failed to upload file";
    protected static final String failedReadFileError_Message="Failed to read file";

    public InternalServerErrorException(String errorMessage) {
        super(errorMessage);
    }

    public static String failedUploadFile()
    {
        return failedUploadFileError_Message;
    }
    public static String failedReadFile()
    {
        return failedReadFileError_Message;
    }
}
