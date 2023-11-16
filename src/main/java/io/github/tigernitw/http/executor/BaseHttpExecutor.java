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

package io.github.tigernitw.http.executor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.tigernitw.http.config.HttpClientConfiguration;
import io.github.tigernitw.http.model.HttpHeader;
import io.github.tigernitw.http.model.QueryParam;
import io.github.tigernitw.http.util.HttpUtils;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;

public abstract class BaseHttpExecutor implements HttpExecutor {

    protected CloseableHttpClient httpClient;
    protected HttpClientConfiguration.Endpoint endpoint;
    protected String path;
    protected List<HttpHeader> httpHeaders;
    protected List<QueryParam> queryParams;
    protected ObjectMapper objectMapper;

    protected BaseHttpExecutor(
            CloseableHttpClient httpClient,
            HttpClientConfiguration.Endpoint endpoint,
            String path,
            List<HttpHeader> httpHeaders,
            List<QueryParam> queryParams,
            ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.endpoint = endpoint;
        this.path = path;
        this.httpHeaders = httpHeaders;
        this.queryParams =queryParams;
        this.objectMapper = objectMapper;
    }


    public abstract HttpUriRequest getRequest(URI uri) throws JsonProcessingException;

    protected Header[] addHeaders() {
        return Objects.nonNull(httpHeaders)
                ? httpHeaders.stream()
                .map(httpHeader -> new BasicHeader(httpHeader.getName(), httpHeader.getValue()))
                .toArray(Header[]::new)
                : null;
    }

    protected void preconditions() {
        Objects.requireNonNull(httpClient, "client can't be null");
    }

    @Override
    public <T> T execute(Class<T> responseType) throws Exception {
        preconditions();
        URI uri = HttpUtils.fetchUrl(endpoint, path, queryParams);
        HttpUriRequest request = getRequest(uri);
        try (final CloseableHttpResponse response = httpClient.execute(request)) {
            final byte[] responseBody = HttpUtils.body(response.getEntity());
            if (Objects.isNull(responseBody)) {
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
    public <T> T execute(TypeReference<T> typeReference) throws Exception {
        preconditions();
        URI uri = HttpUtils.fetchUrl(endpoint, path, queryParams);
        HttpUriRequest request = getRequest(uri);
        try (final CloseableHttpResponse response = httpClient.execute(request)) {
            final byte[] responseBody = HttpUtils.body(response.getEntity());
            return objectMapper.readValue(responseBody, typeReference);
        }
    }
}
