package org.cosmic.backend.globals.handlers;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.cosmic.backend.globals.dto.ErrorResponse;
import org.cosmic.backend.globals.exceptions.BadRequestException;
import org.cosmic.backend.globals.exceptions.NotFoundException;
import org.cosmic.backend.globals.exceptions.UnAuthorizationException;
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
    @ExceptionHandler(UnAuthorizationException.class)
    public ResponseEntity<ErrorResponse> handlerUnAuthorizationException(UnAuthorizationException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handlerBadRequestException(BadRequestException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerNotFoundException(NotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> notValid(MethodArgumentNotValidException e, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_ACCEPTABLE.value(),
                e.getAllErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.joining(", "))
        );

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorResponse);
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handlerHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), "request body is empty");
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(errorResponse);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponse> handlerJwtException(JwtException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Token invalid");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }
}
