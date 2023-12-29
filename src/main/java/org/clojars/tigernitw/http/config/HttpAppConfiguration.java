package org.clojars.tigernitw.http.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@Configuration
@ConfigurationProperties(prefix = "app-http-config")
public class HttpAppConfiguration {

  private String applicationName = "application";

  @NotBlank private String requestIdHeader = "x-request-id";
}
