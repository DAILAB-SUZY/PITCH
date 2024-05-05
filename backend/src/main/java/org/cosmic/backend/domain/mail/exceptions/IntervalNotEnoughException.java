package org.cosmic.backend.domain.mail.exceptions;

public class IntervalNotEnoughException extends RuntimeException {
    public IntervalNotEnoughException() {
        super("Not even 30 seconds passed");
    }
}
