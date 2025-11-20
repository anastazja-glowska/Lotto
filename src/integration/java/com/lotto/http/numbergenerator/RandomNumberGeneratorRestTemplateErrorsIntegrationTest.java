package com.lotto.http.numbergenerator;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.http.Fault;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.lotto.WireMockLottoResponse;
import com.lotto.domain.numbergenerator.RandomNumbersGenerable;
import com.lotto.domain.numbergenerator.dto.SixRandomNumbersDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.ResourceAccessException;

import java.util.Set;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertAll;

class RandomNumberGeneratorRestTemplateErrorsIntegrationTest implements WireMockLottoResponse {


    public static final String CONTENT_TYPE_HEADER_KEY = "Content-Type";
    public static final String CONTENT_TYPE_VALUE = "application/json";

    @RegisterExtension
    public static WireMockExtension wireMockServer = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    RandomNumbersGenerable randomNumbersGenerable = new RandomNumberGeneratorRestTemplateIntegrationTestConfig()
                    .remoteNumberGeneratorClient(wireMockServer.getPort(), 1000, 1000);


    @Test
    @DisplayName("Should return null numbers when connection was reset by peer")
    void should_return_null_numbers_when_connection_was_reset_by_peer(){
        // given

        wireMockServer.stubFor(WireMock.get("/api/v1.0/random?min=1&max=99&count=6")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(CONTENT_TYPE_HEADER_KEY, CONTENT_TYPE_VALUE)
                        .withFault(Fault.CONNECTION_RESET_BY_PEER)));



        // when
        Throwable throwable = catchThrowable(() -> randomNumbersGenerable
                .generateSixRandomNumber(6, 1, 99));

        //then
        assertAll(
                () -> assertThat(throwable).isInstanceOf(ResourceAccessException.class),
                () -> assertThat(throwable.getMessage()).isEqualTo("500 INTERNAL SERVER ERROR")
        );
    }




}
