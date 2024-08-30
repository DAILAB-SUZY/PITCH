package org.cosmic.backend.domain.post.exceptions;

import org.cosmic.backend.globals.exceptions.NotFoundException;

public class NotFoundReplyException extends NotFoundException {
    public NotFoundReplyException(String errorMessage) {
        super(errorMessage);
    }
    public NotFoundReplyException(){
        this("Not Found Reply");
    }
}