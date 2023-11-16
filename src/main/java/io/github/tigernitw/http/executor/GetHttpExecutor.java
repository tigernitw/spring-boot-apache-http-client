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
import io.github.tigernitw.http.model.QueryParam;
import lombok.Builder;
import org.apache.http.Header;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;

import java.net.URI;
import java.util.List;

public class GetHttpExecutor extends BaseHttpExecutor {

    @Builder
    public GetHttpExecutor(CloseableHttpClient httpClient, HttpClientConfiguration.Endpoint endpoint, String path, List<QueryParam> queryParams, List<HttpHeader> httpHeaders, ObjectMapper objectMapper) {
        super(httpClient, endpoint, path, httpHeaders, queryParams, objectMapper);
    }

    @Override
    public HttpUriRequest getRequest(URI uri) throws JsonProcessingException {
        Header[] headers = addHeaders();
        var httpGet = new HttpGet(uri);
        httpGet.setHeaders(headers);
        return httpGet;
    }
}
