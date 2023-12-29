package org.clojars.tigernitw.http.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.http.impl.client.CloseableHttpClient;
import org.clojars.tigernitw.http.config.HttpAppConfiguration;
import org.clojars.tigernitw.http.config.HttpClientConfiguration;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HttpRequest {

  private HttpAppConfiguration httpAppConfiguration;
  private CloseableHttpClient httpClient;
  private HttpClientConfiguration.EndpointConfig endpoint;
  private String path;
  private HttpMethod httpMethod;
  private List<HttpHeader> httpHeaders;
  private List<QueryParam> queryParams;
  private HttpRequestData httpRequestData;
  private ObjectMapper objectMapper;
  private HttpClientConfiguration.TracingConfig tracingConfig;
}
