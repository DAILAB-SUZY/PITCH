package org.cosmic.backend.globals.exceptions;

import java.io.IOException;

public class FileNotFoundException extends IOException {
    protected static final String failedUploadFileError_Message="Exist AlbumLike Exception";
    public FileNotFoundException(String errorMessage) {
        super(errorMessage);
    }
    public static String failedUploadFile()
    {
        return failedUploadFileError_Message;
    }
}
