package org.cosmic.backend.globals.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
  INVALID_TOKEN(401, HttpStatus.UNAUTHORIZED, "unValid Token");

  private final int code;
  private final HttpStatus httpStatus;
  private final String message;
}
