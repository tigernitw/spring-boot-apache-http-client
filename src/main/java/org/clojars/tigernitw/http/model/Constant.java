package org.clojars.tigernitw.http.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constant {

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class Monitoring {
    public static final String X_REQUEST_ID = "x-request-id";
  }
}
