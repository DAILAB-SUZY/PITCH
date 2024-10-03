package org.cosmic.backend.domain.musicDna.exceptions;
import org.cosmic.backend.globals.exceptions.NotFoundException;

public class NotFoundEmotionException extends NotFoundException {
    public NotFoundEmotionException(String errorMessage) {
        super(errorMessage);
    }

    public NotFoundEmotionException(){
        this(getNotFoundEmotionError());
    }
}