package org.cosmic.backend.domain.File.exceptions;


public class ImageSaveFailedException extends Throwable {
    public ImageSaveFailedException(String errorMessage) {
        super(errorMessage);
    }

}
