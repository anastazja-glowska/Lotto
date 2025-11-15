package com.lotto.infrastructure.numbergenerator.http;


import lombok.Builder;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Builder
@ConfigurationProperties(prefix = "lotto.generator-numbers.http.config")
public record RandomGeneratorRestTemplateConfig(long connectTimeout, long readTimeout) {
}
