package org.cosmic.backend.domain.playList.exceptions;

import org.cosmic.backend.globals.exceptions.NotFoundException;

public class NotMatchTrackException extends NotFoundException {
    public NotMatchTrackException(String errorMessage) {
        super(errorMessage);
    }
    public NotMatchTrackException(){
        this("Not Match Track Exception");
    }
}
