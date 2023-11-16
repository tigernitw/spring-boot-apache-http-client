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

package io.github.tigernitw.http.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

/**
 * {@link io.github.tigernitw.http.config.HttpClientConfiguration}  Configuration class for HTTP client
 * comes up.
 */

@Data
@Validated
public class HttpClientConfiguration {

    @Valid
    private Endpoint endpoint;

    @Min(10)
    @Max(1024)
    private int connections = 10;

    @Max(86400)
    private int idleTimeOutSeconds = 30;

    @Max(86400000)
    private int connectTimeoutMs = 10000;

    @Max(86400000)
    private int opTimeoutMs = 10000;


    @Data
    public static class Endpoint {

        @NotBlank
        private String host;

        private int port;

        private boolean secure;

    }

}