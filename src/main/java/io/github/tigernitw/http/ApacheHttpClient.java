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

package io.github.tigernitw.http;

import io.github.tigernitw.http.config.HttpClientConfiguration;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;

import java.util.concurrent.TimeUnit;

public class ApacheHttpClient {

    private static final int DEFAULT_CONNECTIONS = 10;
    private static final int CONNECTION_TIMEOUT_IN_MS = 10000;
    private static final int OP_TIMEOUT_IN_MS = 10000;
    private static final int IDLE_TIMEOUT_IN_SEC = 30;

    public static CloseableHttpClient createClient(HttpClientConfiguration httpConfig) {
        var connections = httpConfig.getConnections() == 0 ? DEFAULT_CONNECTIONS : httpConfig.getConnections();
        var connectionTimeout = httpConfig.getConnectTimeoutMs() == 0 ? CONNECTION_TIMEOUT_IN_MS : httpConfig.getConnectTimeoutMs();
        var socketTimeout = httpConfig.getOpTimeoutMs() == 0 ? OP_TIMEOUT_IN_MS : httpConfig.getOpTimeoutMs();
        var idleTimeOut = httpConfig.getIdleTimeOutSeconds() == 0 ? IDLE_TIMEOUT_IN_SEC : httpConfig.getIdleTimeOutSeconds();
        var requestConfig = RequestConfig.custom().setConnectTimeout(connectionTimeout).setSocketTimeout(socketTimeout).setConnectionRequestTimeout(connectionTimeout).build();

        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(connections); // Maximum connections
        connectionManager.setDefaultMaxPerRoute(connections); // Maximum connections per route

        ConnectionKeepAliveStrategy keepAliveStrategy = new DefaultConnectionKeepAliveStrategy() {
            @Override
            public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
                var keepAlive = super.getKeepAliveDuration(response, context);
                if (keepAlive == -1) {
                    // Keep connections alive 5 seconds if a keep-alive value has not explicitly set by the server
                    keepAlive = 5 * 1000;
                }
                return keepAlive;
            }
        };
        return HttpClients.custom().setConnectionManager(connectionManager).setDefaultRequestConfig(requestConfig).setKeepAliveStrategy(keepAliveStrategy).evictIdleConnections(idleTimeOut, TimeUnit.SECONDS).build();
    }

}
