package org.cosmic.backend.globals.handlers;

import jakarta.servlet.http.HttpServletRequest;
import org.cosmic.backend.domain.auth.exceptions.CredentialNotMatchException;
import org.cosmic.backend.domain.mail.exceptions.ExistEmailException;
import org.cosmic.backend.domain.mail.exceptions.IntervalNotEnoughException;
import org.cosmic.backend.domain.user.exceptions.*;
import org.cosmic.backend.globals.dto.ErrorResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    //이미 존재
    @ExceptionHandler(ExistEmailException.class)
    public ResponseEntity<ErrorResponse> handlerExistEmailException(ExistEmailException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }
    //랜덤코드 일치하지않음
    @ExceptionHandler(IntervalNotEnoughException.class)
    public ResponseEntity<ErrorResponse> handlerIntervalNotEnoughException(IntervalNotEnoughException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }
    //이메일 존재x
    @ExceptionHandler(NotExistEmailException.class)
    public ResponseEntity<ErrorResponse> handlerNotExistEmailException(NotExistEmailException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
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
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
    //null데이터가 있을 때
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> notValid(MethodArgumentNotValidException e, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_ACCEPTABLE.value(),
                e.getAllErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.joining())
        );

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorResponse);
    }
    //데이터가 들어오지 않았을 때
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handlerHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), "request body is empty");
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(errorResponse);
    }

    @ExceptionHandler(CredentialNotMatchException.class)
    public ResponseEntity<ErrorResponse> handlerCredentialNotMatchException(CredentialNotMatchException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }
}
