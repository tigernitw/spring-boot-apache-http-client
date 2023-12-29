/*
 * Copyright (c) 2023 Shiva Samadhiya <shiva94.nitw@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.clojars.tigernitw.http.executor;

import static org.clojars.tigernitw.http.model.Constant.Monitoring.X_REQUEST_ID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;
import org.clojars.tigernitw.http.config.HttpAppConfiguration;
import org.clojars.tigernitw.http.config.HttpClientConfiguration;
import org.clojars.tigernitw.http.exception.ErrorResponseException;
import org.clojars.tigernitw.http.model.ExtractedResponse;
import org.clojars.tigernitw.http.model.HttpHeader;
import org.clojars.tigernitw.http.model.QueryParam;
import org.clojars.tigernitw.http.util.CollectionUtils;
import org.clojars.tigernitw.http.util.HttpUtils;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;

@Slf4j
public abstract class BaseHttpExecutor implements HttpExecutor {

  protected HttpAppConfiguration httpAppConfiguration;
  protected CloseableHttpClient httpClient;
  protected HttpClientConfiguration.EndpointConfig endpoint;
  protected HttpClientConfiguration.TracingConfig tracingConfig;
  protected String path;
  protected List<HttpHeader> httpHeaders;
  protected List<QueryParam> queryParams;
  protected ObjectMapper objectMapper;

  protected BaseHttpExecutor(
      HttpAppConfiguration httpAppConfiguration,
      CloseableHttpClient httpClient,
      HttpClientConfiguration.EndpointConfig endpoint,
      HttpClientConfiguration.TracingConfig tracingConfig,
      String path,
      List<HttpHeader> httpHeaders,
      List<QueryParam> queryParams,
      ObjectMapper objectMapper) {
    this.httpAppConfiguration = httpAppConfiguration;
    this.httpClient = httpClient;
    this.endpoint = endpoint;
    this.tracingConfig = tracingConfig;
    this.path = path;
    this.httpHeaders = httpHeaders;
    this.queryParams = queryParams;
    this.objectMapper = objectMapper;
  }

  public abstract HttpUriRequest getRequest(URI uri) throws JsonProcessingException;

  protected Header[] addHeaders() {
    List<Header> headers =
        Objects.nonNull(httpHeaders)
            ? httpHeaders.stream()
                .map(httpHeader -> new BasicHeader(httpHeader.getName(), httpHeader.getValue()))
                .collect(Collectors.toList())
            : new ArrayList<>();
    headers.add(new BasicHeader(X_REQUEST_ID, MDC.get(httpAppConfiguration.getRequestIdHeader())));
    if (Objects.nonNull(tracingConfig)
        && StringUtils.hasLength(tracingConfig.getRequestIdHeader())) {
      headers.add(
          new BasicHeader(
              tracingConfig.getRequestIdHeader(),
              MDC.get(httpAppConfiguration.getRequestIdHeader())));
    }
    return headers.toArray(Header[]::new);
  }

  protected void preconditions() {
    Objects.requireNonNull(httpClient, "client can't be null");
    Objects.requireNonNull(httpAppConfiguration, "httpAppConfiguration can't be null");
  }

  @Override
  public <T> T execute(
      Class<T> responseType, Function<ExtractedResponse, T> nonSuccessResponseConsumer)
      throws Exception {
    preconditions();
    URI uri = HttpUtils.fetchUrl(endpoint, path, queryParams);
    HttpUriRequest request = getRequest(uri);
    try (final CloseableHttpResponse response = httpClient.execute(request)) {
      final byte[] responseBody = HttpUtils.body(response.getEntity());
      int statusCode = response.getStatusLine().getStatusCode();
      if (!HttpUtils.isSuccessful(statusCode)) {
        if (nonSuccessResponseConsumer != null) {
          return nonSuccessResponseConsumer.apply(extract(response, responseBody));
        }
        String body = CollectionUtils.convertByteArrayToString(responseBody);
        log.error(
            "BaseHttpExecutor ::  execute :: http call failed statusCode: {} request: {} response: {}",
            statusCode,
            request,
            body);
        throw new ErrorResponseException("Service http call failure", null, statusCode, body);
      }
      if (CollectionUtils.isNullOrEmpty(responseBody)) {
        return null;
      } else {
        if (byte[].class.equals(responseType)) {
          return (T) responseBody;
        } else if (String.class.equals(responseType)) {
          return responseType.cast(new String(responseBody, Charset.defaultCharset()));
        } else {
          return objectMapper.readValue(responseBody, responseType);
        }
      }
    }
  }

  @Override
  public <T> T execute(
      TypeReference<T> typeReference, Function<ExtractedResponse, T> nonSuccessResponseConsumer)
      throws Exception {
    preconditions();
    URI uri = HttpUtils.fetchUrl(endpoint, path, queryParams);
    HttpUriRequest request = getRequest(uri);
    try (final CloseableHttpResponse response = httpClient.execute(request)) {
      final byte[] responseBody = HttpUtils.body(response.getEntity());
      return objectMapper.readValue(responseBody, typeReference);
    }
  }

  private ExtractedResponse extract(CloseableHttpResponse response, byte[] responseBody) {
    return ExtractedResponse.builder()
        .body(responseBody)
        .code(response.getStatusLine().getStatusCode())
        .build();
  }
}
