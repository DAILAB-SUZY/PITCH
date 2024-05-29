package org.cosmic.backend.domain.musicDNA.exceptions;

import org.cosmic.backend.globals.exceptions.BadRequestException;

public class NotMatchMusicDnaCountException extends BadRequestException {
    public NotMatchMusicDnaCountException(String errorMessage) {
        super(errorMessage);
    }

    public NotMatchMusicDnaCountException(){
        this("Need 4 MusicDna");
    }
}
