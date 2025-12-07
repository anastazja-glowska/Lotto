package com.lotto.infrastructure.numbergenerator.http;

import com.lotto.domain.numbergenerator.RandomNumbersGenerable;
import com.lotto.domain.numbergenerator.dto.SixRandomNumbersDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Log4j2
class RandomNumbersGeneratorRestTemplate implements RandomNumbersGenerable {

    private static final String RANDOM_NUMBER_SERVICE_PATH = "/api/v1.0/random";
    private static final int MAXIMAL_WINNING_NUMBERS = 6;

    private final RestTemplate restTemplate;
    private final String uri;
    private final int port;

    @Override
    public SixRandomNumbersDto generateSixRandomNumber(int count, int lowerBand, int upperBand) {

        final HttpHeaders headers = new HttpHeaders();
        final HttpEntity<HttpHeaders> requestEntity = new HttpEntity<>(headers);

        try {

            ResponseEntity<List<Integer>> responseEntity = makeGetRequest(count, lowerBand, upperBand, requestEntity);
            Set<Integer> sixDistinctRandomNumbers = getSixDistinctRandomNumbers(responseEntity);
            if (sixDistinctRandomNumbers.size() < MAXIMAL_WINNING_NUMBERS) {

                log.error("Six distinct random numbers were not generated, size is not correct!");
                return generateSixRandomNumber(count, lowerBand, upperBand);
            }

            log.info("Winning numbers generated with success!");
            return SixRandomNumbersDto.builder()
                    .numbers(sixDistinctRandomNumbers)
                    .build();


        } catch (ResourceAccessException e) {
            log.error("Error while getting generated winning numbers!");

            throw new ResourceAccessException("500 INTERNAL SERVER ERROR");
        }

    }

    private Set<Integer> getSixDistinctRandomNumbers(ResponseEntity<List<Integer>> entity) {

        List<Integer> numbers = entity.getBody();
        if (numbers == null || numbers.isEmpty()) {
            log.error("No numbers found");
            throw new NoContentException("204 NO_CONTENT");
        }

        HashSet<Integer> distinctNumbers = new HashSet<>(numbers);
        return distinctNumbers
                .stream()
                .limit(MAXIMAL_WINNING_NUMBERS)
                .collect(Collectors.toSet());
    }

    private ResponseEntity<List<Integer>> makeGetRequest(int count, int lowerBand, int upperBand,
                                                         HttpEntity<HttpHeaders> entity) {

        final String uriString = UriComponentsBuilder.fromHttpUrl(getUrlForService(RANDOM_NUMBER_SERVICE_PATH))
                .queryParam("min", lowerBand)
                .queryParam("max", upperBand)
                .queryParam("count", count)
                .toUriString();

        ResponseEntity<List<Integer>> response = restTemplate.exchange(uriString,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {
                });
        return response;
    }

    private String getUrlForService(String service) {
        return uri + ":" + port + service;
    }
}
