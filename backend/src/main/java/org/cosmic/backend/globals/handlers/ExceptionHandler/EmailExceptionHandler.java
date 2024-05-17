package org.cosmic.backend.globals.handlers.ExceptionHandler;

import org.cosmic.backend.domain.mail.exceptions.ExistEmailException;
import org.cosmic.backend.domain.mail.exceptions.IntervalNotEnoughException;
import org.cosmic.backend.domain.user.exceptions.NotExistEmailException;
import org.cosmic.backend.globals.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class EmailExceptionHandler {
    //이미 존재
    @ExceptionHandler(ExistEmailException.class)
    public ResponseEntity<ErrorResponse> handlerExistEmailException(ExistEmailException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    //이메일 존재x
    @ExceptionHandler(NotExistEmailException.class)
    public ResponseEntity<ErrorResponse> handlerNotExistEmailException(NotExistEmailException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    //랜덤코드 일치하지않음
    @ExceptionHandler(IntervalNotEnoughException.class)
    public ResponseEntity<ErrorResponse> handlerIntervalNotEnoughException(IntervalNotEnoughException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }
}
