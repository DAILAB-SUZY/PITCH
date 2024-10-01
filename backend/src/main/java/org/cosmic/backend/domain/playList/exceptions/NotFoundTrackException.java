package org.cosmic.backend.domain.playList.exceptions;

import org.cosmic.backend.globals.exceptions.NotFoundException;

public class NotFoundTrackException extends NotFoundException {
    public NotFoundTrackException(String errorMessage) {
        super(errorMessage);
    }
    public NotFoundTrackException(){
        this("Not Found Track Exception");
    }
}

