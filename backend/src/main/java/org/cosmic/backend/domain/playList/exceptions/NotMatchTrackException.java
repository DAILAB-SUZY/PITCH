package org.cosmic.backend.domain.playList.exceptions;

import org.cosmic.backend.globals.exceptions.BadRequestException;

public class NotMatchTrackException extends BadRequestException {
    public NotMatchTrackException(String errorMessage) {
        super(errorMessage);
    }
    public NotMatchTrackException(){
        this("Not Match Track Exception");
    }
}
