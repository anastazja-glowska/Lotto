package com.lotto.infrastructure.numbergenerator.http;

import com.lotto.domain.numbergenerator.RandomNumbersGenerable;
import com.lotto.domain.numbergenerator.dto.SixRandomNumbersDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Log4j2
class RandomNumbersGeneratorRestTemplate implements RandomNumbersGenerable {

    private final RestTemplate restTemplate;
    private final String uri;
    private final int port;

    @Override
    public SixRandomNumbersDto generateSixRandomNumber() {

        final HttpHeaders headers = new HttpHeaders();
        final HttpEntity<HttpHeaders> requestEntity = new HttpEntity<>(headers);
        String url = UriComponentsBuilder.fromHttpUrl("http://ec2-3-127-218-34.eu-central-1.compute.amazonaws.com:9090/api/v1.0/random")
                .queryParam("min", 1)
                .queryParam("max", 99)
                .queryParam("count", 25)
                .toUriString();

        ResponseEntity<List<Integer>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<>() {
                }
        );

        List<Integer> numbers = response.getBody();
        log.info(numbers);
        return new SixRandomNumbersDto(numbers.stream().collect(Collectors.toSet()));

    }
}
