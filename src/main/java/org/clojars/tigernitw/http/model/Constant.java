package org.clojars.tigernitw.http.model;

import java.util.Arrays;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constant {

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class Monitoring {
    public static final String X_REQUEST_ID = "x-request-id";
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class RequestHeaders {

    public static final String TRANSFER_ENCODING = "transfer-encoding";
    public static final String CONTENT_LENGTH = "content-length";

    public static final List<String> REMOVABLE_HEADERS =
        Arrays.asList(TRANSFER_ENCODING, CONTENT_LENGTH);
  }
}
