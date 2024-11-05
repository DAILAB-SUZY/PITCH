package org.cosmic.backend.globals.exceptions;

public class InvalidTypeException extends BadRequestException {

  public InvalidTypeException(String message) {
    super(message);
  }

  public InvalidTypeException() {
    this(getInvalidTypeError());
  }

}
