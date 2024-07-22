package org.cosmic.backend.domain.albumChat.exceptions;

import org.cosmic.backend.globals.exceptions.NotFoundException;

public class NotFoundAlbumChatException extends NotFoundException {
    public NotFoundAlbumChatException(String errorMessage) {
        super(errorMessage);
    }
    public NotFoundAlbumChatException(){
        this("Not found albumChat page");
    }
}

