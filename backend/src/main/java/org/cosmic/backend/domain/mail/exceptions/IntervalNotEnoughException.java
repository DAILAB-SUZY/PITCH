package org.cosmic.backend.domain.mail.exceptions;

import org.cosmic.backend.globals.exceptions.UnAuthorizationException;

public class IntervalNotEnoughException extends UnAuthorizationException {
    public IntervalNotEnoughException() {
        super("Not even 30 seconds passed");
    }
}
