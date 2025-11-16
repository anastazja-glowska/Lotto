package com.lotto.infrastructure.numbergenerator.http;

import com.lotto.domain.numbergenerator.RandomNumbersGenerable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RandomGeneratorClientConfig {


    @Bean
    public RestTemplateResponseErrorHandler restTemplateResponseErrorHandler(){
        return new RestTemplateResponseErrorHandler();
    }

    @Bean
    public RestTemplate restTemplate(
            RestTemplateResponseErrorHandler restTemplateResponseErrorHandler,
            RandomGeneratorRestTemplateConfig randomGeneratorRestTemplateConfig) {

        return new RestTemplateBuilder()
                .errorHandler(restTemplateResponseErrorHandler)
                .setConnectTimeout(Duration.ofMillis(randomGeneratorRestTemplateConfig.connectTimeout()))
                .setReadTimeout(Duration.ofMillis(randomGeneratorRestTemplateConfig.readTimeout()))
                .build();

    }

    @Bean
    public RandomNumbersGenerable remoteNumberGeneratorClient(RestTemplate restTemplate,
                                                              @Value("${lotto.number-generator.http.client.config.uri}") String uri,
                                                              @Value("${lotto.number-generator.http.client.config.port}") int port){

        return new RandomNumbersGeneratorRestTemplate(restTemplate, uri, port);
    }
}
