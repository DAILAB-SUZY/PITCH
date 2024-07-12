package org.cosmic.backend.domain.albumChat.exception;

import org.cosmic.backend.globals.exceptions.NotFoundException;

public class NotFoundAlbumChatCommentException extends NotFoundException {
    public NotFoundAlbumChatCommentException(String errorMessage) {
        super(errorMessage);
    }
    public NotFoundAlbumChatCommentException(){
        this("Not found albumChatComment");
    }
}

