package org.cosmic.backend.globals.handlers;

import org.cosmic.backend.domain.mail.exceptions.ExistEmailException;
import org.cosmic.backend.domain.mail.exceptions.IntervalNotEnoughException;
import org.cosmic.backend.globals.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ExistEmailException.class)
    public ResponseEntity<ErrorResponse> handlerExistEmailException(ExistEmailException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(IntervalNotEnoughException.class)
    public ResponseEntity<ErrorResponse> handlerIntervalNotEnoughException(IntervalNotEnoughException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
