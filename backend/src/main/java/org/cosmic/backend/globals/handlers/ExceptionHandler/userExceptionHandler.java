package org.cosmic.backend.globals.handlers.ExceptionHandler;

import org.cosmic.backend.domain.user.exceptions.NotMatchConditionException;
import org.cosmic.backend.domain.user.exceptions.NotMatchPasswordException;
import org.cosmic.backend.globals.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class userExceptionHandler {
    //비밀번호 조건 충족x
    @ExceptionHandler(NotMatchConditionException.class)
    public ResponseEntity<ErrorResponse> handlerNotMatchConditionException(NotMatchConditionException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    //확인 비번과 비번이 다르다
    @ExceptionHandler(NotMatchPasswordException.class)
    public ResponseEntity<ErrorResponse> handlerNotMatchPasswordException(NotMatchPasswordException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
}
