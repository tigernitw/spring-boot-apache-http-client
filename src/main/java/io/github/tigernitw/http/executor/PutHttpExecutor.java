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
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.tigernitw.http.config.HttpClientConfiguration;
import io.github.tigernitw.http.model.HttpHeader;
import io.github.tigernitw.http.model.HttpRequestData;
import io.github.tigernitw.http.model.QueryParam;
import lombok.Builder;
import org.apache.http.Header;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;

import java.net.URI;
import java.util.List;
import java.util.Objects;

public class PutHttpExecutor extends BaseHttpExecutor {

    private final HttpRequestData httpRequestData;

    @Builder
    public PutHttpExecutor(CloseableHttpClient httpClient, HttpClientConfiguration.Endpoint endpoint, String path, HttpRequestData httpRequestData, List<HttpHeader> httpHeaders, List<QueryParam> queryParams, ObjectMapper objectMapper) {
        super(httpClient, endpoint, path, httpHeaders, queryParams, objectMapper);
        this.httpRequestData = httpRequestData;
    }

    @Override
    public HttpUriRequest getRequest(URI uri) throws JsonProcessingException {
        Header[] headers = addHeaders();

        var httpPut = new HttpPut(uri);
        if (Objects.nonNull(httpRequestData)) {
            var requestEntity =
                    new StringEntity(
                            objectMapper.writeValueAsString(httpRequestData.getData()), httpRequestData.getContentType());
            httpPut.setEntity(requestEntity);
        }
        httpPut.setHeaders(headers);
        return httpPut;
    }
}
