package com.lotto.cache.redis;

import com.lotto.BaseIntegrationTest;
import com.lotto.WireMockLottoResponse;
import com.lotto.domain.numberreceiver.dto.InputNumbersResponseDto;
import com.lotto.domain.resultannouncer.ResultAnnouncerFacade;
import com.lotto.infrastructure.loginandregister.controller.dto.JwtResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RedisLottoCacheIntegrationTest extends BaseIntegrationTest implements WireMockLottoResponse {

    private static final String INPUT_NUMBERS_ENDPOINT = "/inputNumbers";
    private static final String TOKEN_ENDPOINT = "/token";
    private static final String REGISTER_ENDPOINT = "/register";
    private static final String RESULTS_ENDPOINT = "/results";
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";

    @Container
    private static final GenericContainer<?> REDIS;

    @SpyBean
    ResultAnnouncerFacade resultAnnouncerFacade;

    @Autowired
    CacheManager cacheManager;

    static {
        REDIS = new GenericContainer<>("redis").withExposedPorts(6379);
        REDIS.start();
    }

    @DynamicPropertySource
    public static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
        registry.add("spring.data.redis.port", () -> REDIS.getFirstMappedPort().toString() );
        registry.add("spring.cache.type", () -> "redis");
        registry.add("spring.cache.redis.time-to-live", () -> "PT1S");
    }

    @Test
    @DisplayName("Should save results/ticketId to cache and then invalidate by time to live")
    void should_save_results_ticketId_to_cache_and_then_invalidate_by_time_to_live() throws Exception {
        // step 1: someUser was registered with somePassword

        //given && when
        ResultActions registeredAction = mockMvc.perform(post(REGISTER_ENDPOINT)
                .content(retrieveSomeUserWithSomePassword())
                .contentType(MediaType.APPLICATION_JSON));

        //then
        registeredAction.andExpect(status().isCreated());


        // step 2: user log in

        //given && when
        ResultActions performedToken = mockMvc.perform(post(TOKEN_ENDPOINT)
                .content(retrieveSomeUserWithSomePassword())
                .contentType(MediaType.APPLICATION_JSON));

        //then
        MvcResult returnedResultWithToken = performedToken.andExpect(status().isOk()).andReturn();
        String resultJson = returnedResultWithToken.getResponse().getContentAsString();
        JwtResponseDto jwtResponseDto = objectMapper.readValue(resultJson, JwtResponseDto.class);
        String token = jwtResponseDto.token();


        // step 3: user post numbers to take part in lotto game

        //given && when

        ResultActions inputNumbersResult = mockMvc.perform(post(INPUT_NUMBERS_ENDPOINT)
                        .header(AUTHORIZATION, BEARER + token)
                .content(retrieveInputNumbers())
                .contentType(MediaType.APPLICATION_JSON));

        //then
        MvcResult inputNumbersMvc = inputNumbersResult.andExpect(status().isOk()).andReturn();
        String inputNumbersJson = inputNumbersMvc.getResponse().getContentAsString();
        InputNumbersResponseDto inputNumbersResponseDto = objectMapper
                .readValue(inputNumbersJson, InputNumbersResponseDto.class);

        String ticketId = inputNumbersResponseDto.ticketDto().ticketId();


        // step 4: should save to cache results/tickerId request

        //given && when
        mockMvc.perform(get(RESULTS_ENDPOINT +"/" +  ticketId)
                .header(AUTHORIZATION, BEARER + token)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        verify(resultAnnouncerFacade, times(1)).checkPlayResultByHash(ticketId);
        assertThat(cacheManager.getCacheNames().contains("lotto")).isTrue();

        // step 5: cache should be invalidated
        await()
                .atMost(Duration.ofSeconds(4))
                .pollInterval(Duration.ofSeconds(1))
                .untilAsserted(() -> {
                    mockMvc.perform(get("/results/" +  ticketId)
                            .header(AUTHORIZATION, BEARER + token)
                            .contentType(MediaType.APPLICATION_JSON));

                    verify(resultAnnouncerFacade, atLeast(2)).checkPlayResultByHash(ticketId);
                });

    }


}
