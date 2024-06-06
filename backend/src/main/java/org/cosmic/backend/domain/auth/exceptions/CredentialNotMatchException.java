package org.cosmic.backend.domain.auth.exceptions;

import org.cosmic.backend.globals.exceptions.UnAuthorizationException;

public class CredentialNotMatchException extends UnAuthorizationException {
    public CredentialNotMatchException(){
        super("Email or Password is wrong");
    }
}
