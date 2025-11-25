package com.lotto.http.numbergenerator;

import com.lotto.domain.numbergenerator.RandomGenerable;
import com.lotto.domain.numbergenerator.RandomNumbersGenerable;
import com.lotto.infrastructure.numbergenerator.http.RandomGeneratorClientConfig;
import com.lotto.infrastructure.numbergenerator.http.RandomGeneratorRestTemplateConfig;
import org.springframework.web.client.RestTemplate;

public class RandomNumberGeneratorRestTemplateIntegrationTestConfig extends RandomGeneratorClientConfig {

    public static final String WIRE_MOCK_HOST = "http://localhost";

    public RandomNumbersGenerable remoteNumberGeneratorClient(int port, int connectionTimeout, int readTimeout){

        RandomGeneratorRestTemplateConfig templateConfig =
                new RandomGeneratorRestTemplateConfig(connectionTimeout, readTimeout);

        RestTemplate restTemplate = restTemplate(restTemplateResponseErrorHandler(), templateConfig);
        return remoteNumberGeneratorClient(restTemplate, WIRE_MOCK_HOST, port);
    }
}
