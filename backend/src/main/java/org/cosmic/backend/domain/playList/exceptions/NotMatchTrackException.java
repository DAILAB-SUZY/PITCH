package org.cosmic.backend.domain.playList.exceptions;

import org.cosmic.backend.globals.exceptions.UnAuthorizationException;

public class NotMatchTrackException extends UnAuthorizationException {
    public NotMatchTrackException(String errorMessage) {
        super(errorMessage);
    }
    public NotMatchTrackException(){
        this("Not Match Track Exception");
    }
}
