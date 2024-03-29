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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.Builder;
import org.apache.http.Header;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.clojars.tigernitw.http.config.HttpAppConfiguration;
import org.clojars.tigernitw.http.config.HttpClientConfiguration;
import org.clojars.tigernitw.http.model.HttpHeader;
import org.clojars.tigernitw.http.model.HttpRequestData;
import org.clojars.tigernitw.http.model.QueryParam;

public class PutHttpExecutor extends BaseHttpExecutor {

  private final HttpRequestData httpRequestData;

  @Builder
  public PutHttpExecutor(
      HttpAppConfiguration httpAppConfiguration,
      CloseableHttpClient httpClient,
      HttpClientConfiguration.EndpointConfig endpoint,
      HttpClientConfiguration.TracingConfig tracingConfig,
      String path,
      HttpRequestData httpRequestData,
      Set<HttpHeader> httpHeaders,
      List<QueryParam> queryParams,
      ObjectMapper objectMapper) {
    super(
        httpAppConfiguration,
        httpClient,
        endpoint,
        tracingConfig,
        path,
        httpHeaders,
        queryParams,
        objectMapper);
    this.httpRequestData = httpRequestData;
  }

  @Override
  public HttpUriRequest getRequest(URI uri) throws JsonProcessingException {
    Header[] headers = addHeaders();

    var httpPut = new HttpPut(uri);
    if (Objects.nonNull(httpRequestData)) {
      var requestEntity =
          new StringEntity(
              objectMapper.writeValueAsString(httpRequestData.getData()),
              httpRequestData.getContentType());
      httpPut.setEntity(requestEntity);
    }
    httpPut.setHeaders(headers);
    return httpPut;
  }
}
