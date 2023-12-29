package org.clojars.tigernitw.http.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ErrorResponseException extends RuntimeException {

  private final int statusCode;
  private final String body;

  public ErrorResponseException(String message, Throwable cause, int statusCode, String body) {
    super(message, cause);
    this.statusCode = statusCode;
    this.body = body;
  }
}
