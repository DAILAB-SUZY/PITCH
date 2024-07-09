package org.cosmic.backend.domain.playList.exceptions;

import org.cosmic.backend.globals.exceptions.NotFoundException;

public class NotFoundTrackIdException extends NotFoundException {
    public NotFoundTrackIdException(String errorMessage) {
        super(errorMessage);
    }
    public NotFoundTrackIdException(){
        this("Not Found TrackId Exception");
    }
}

