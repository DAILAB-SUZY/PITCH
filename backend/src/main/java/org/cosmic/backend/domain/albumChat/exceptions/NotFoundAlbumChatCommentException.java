package org.cosmic.backend.domain.albumChat.exceptions;

import org.cosmic.backend.globals.exceptions.NotFoundException;

public class NotFoundAlbumChatCommentException extends NotFoundException {
    public NotFoundAlbumChatCommentException(String errorMessage) {
        super(errorMessage);
    }
    public NotFoundAlbumChatCommentException(){
        this(getNotFoundAlbumChatCommentError());
    }
}

