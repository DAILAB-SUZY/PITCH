package org.cosmic.backend.domain.File.exceptions;


import org.cosmic.backend.globals.exceptions.InternalServerErrorException;

public class ImageSaveFailedException extends InternalServerErrorException {
    public ImageSaveFailedException(String errorMessage) {
        super(errorMessage);
    }
    public ImageSaveFailedException(){
        this(failedUploadFile());
    }
}
