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

package io.github.tigernitw.http.util;

import io.github.tigernitw.http.config.HttpClientConfiguration;
import io.github.tigernitw.http.model.QueryParam;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpUtils {

    private static final String HTTPS_SCHEME = "https";
    private static final String HTTP_SCHEME = "http";

    public static URI fetchUrl(HttpClientConfiguration.Endpoint endpoint, String path, List<QueryParam> queryParams) throws URISyntaxException {

        List<NameValuePair> nameValuePairs = CollectionUtils.isNullOrEmpty(queryParams) ? null : queryParams.stream()
                .map(queryParam -> new BasicNameValuePair(queryParam.getName(), queryParam.getValue()))
                .collect(Collectors.toList());
        URIBuilder uriBuilder = new URIBuilder().setHost(endpoint.getHost()).setPort(endpoint.getPort()).setPath(path).addParameters(nameValuePairs);

        if (endpoint.isSecure()) {
            uriBuilder.setScheme(HTTPS_SCHEME);
        } else {
            uriBuilder.setScheme(HTTP_SCHEME);
        }
        return uriBuilder.build();
    }

    public static byte[] body(HttpEntity entity) throws IOException {
        return Objects.isNull(entity) ? null : EntityUtils.toByteArray(entity);
    }

}
