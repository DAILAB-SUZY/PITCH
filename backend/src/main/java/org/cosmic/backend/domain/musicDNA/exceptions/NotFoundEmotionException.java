package org.cosmic.backend.domain.musicDNA.exceptions;


import org.cosmic.backend.globals.exceptions.NotFoundException;

public class NotFoundEmotionException extends NotFoundException {
    public NotFoundEmotionException(String errorMessage) {
        super(errorMessage);
    }

    public NotFoundEmotionException(){
        this("Not Found Emotion");
    }
}