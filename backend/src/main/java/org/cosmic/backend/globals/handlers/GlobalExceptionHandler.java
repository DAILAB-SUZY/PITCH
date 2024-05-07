package org.cosmic.backend.globals.handlers;

import org.cosmic.backend.domain.mail.exceptions.ExistEmailException;
import org.cosmic.backend.domain.mail.exceptions.IntervalNotEnoughException;
import org.cosmic.backend.domain.user.exceptions.NotExistEmailException;
import org.cosmic.backend.domain.user.exceptions.NotMatchConditionException;
import org.cosmic.backend.domain.user.exceptions.NotMatchPasswordException;
import org.cosmic.backend.domain.user.exceptions.NullException;
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

    //이메일 존재x
    @ExceptionHandler(NotExistEmailException.class)
    public ResponseEntity<ErrorResponse> handlerNotExistEmailException(NotExistEmailException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    //비밀번호 조건 충족x
    @ExceptionHandler(NotMatchConditionException.class)
    public ResponseEntity<ErrorResponse> handlerNotMatchConditionException(NotMatchConditionException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    //확인 비번과 비번이 다르다
    @ExceptionHandler(NotMatchPasswordException.class)
    public ResponseEntity<ErrorResponse> handlerNotMatchPasswordException(NotMatchPasswordException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    //NULL데이터가 들어온게 있다.
    @ExceptionHandler(NullException.class)
    public ResponseEntity<ErrorResponse> handlerNullException(NullException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
