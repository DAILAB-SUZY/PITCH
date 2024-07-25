package org.cosmic.backend.domain.albumChat.exceptions;

import org.cosmic.backend.globals.exceptions.BadRequestException;

public class NotMatchAlbumChatException extends BadRequestException {
    public NotMatchAlbumChatException(String errorMessage) {
        super(errorMessage);
    }
    public NotMatchAlbumChatException(){
        this(getNotMatchAlbumChatError());
    }
}
