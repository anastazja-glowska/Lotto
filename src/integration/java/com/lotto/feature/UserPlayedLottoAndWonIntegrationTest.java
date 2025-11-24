package com.lotto.feature;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.lotto.BaseIntegrationTest;
import com.lotto.WireMockLottoResponse;
import com.lotto.domain.loginandregister.dto.UserRegisterResponseDto;
import com.lotto.domain.numbergenerator.NumberGeneratorFacade;
import com.lotto.domain.numbergenerator.NumberOutOfRangeException;
import com.lotto.domain.numbergenerator.RandomNumbersGenerable;
import com.lotto.domain.numbergenerator.WinningNumberRepository;
import com.lotto.domain.numbergenerator.WinningNumbers;
import com.lotto.domain.numbergenerator.WinningNumbersNotFoundException;
import com.lotto.domain.numbergenerator.dto.SixRandomNumbersDto;
import com.lotto.domain.numbergenerator.dto.WinningNumbersDto;
import com.lotto.domain.numberreceiver.dto.InputNumbersResponseDto;
import com.lotto.domain.resultannouncer.dto.ResultMessageDto;
import com.lotto.domain.resultchecker.Player;
import com.lotto.domain.resultchecker.PlayerNotFoundException;
import com.lotto.domain.resultchecker.PlayersRepository;
import com.lotto.domain.resultchecker.ResultCheckerFacade;
import com.lotto.domain.resultchecker.dto.AllPlayersDto;
import com.lotto.domain.resultchecker.dto.PlayerDto;
import com.lotto.infrastructure.loginandregister.controller.dto.JwtResponseDto;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.regex.Pattern;


import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertAll;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log4j2
class UserPlayedLottoAndWonIntegrationTest extends BaseIntegrationTest implements WireMockLottoResponse {

    private static final String INPUT_NUMBERS_ENDPOINT = "/inputNumbers";
    private static final String TOKEN_ENDPOINT = "/token";
    private static final String REGISTER_ENDPOINT = "/register";
    private static final String EXTERNAL_SERVICE_ENDPOINT = "/api/v1.0/random?min=1&max=99&count=6";
    private static final String RESULTS_ENDPOINT = "/results";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String CONTENT_TYPE_VALUE = "application/json";

    @Autowired
    NumberGeneratorFacade numberGeneratorFacade;

    @Autowired
    WinningNumberRepository winningNumberRepository;

    @Autowired
    ResultCheckerFacade resultCheckerFacade;

    @Autowired
    PlayersRepository playersRepository;



    private final LocalDateTime drawDate = LocalDateTime.of(2025, 11, 8, 12, 0, 0);

    @BeforeEach
    void setup() {
        winningNumberRepository.deleteAll();

    }



    @Test
    @DisplayName("Should user win and system should generate winners")
    void should_user_win_and_system_should_generate_winners() throws Exception {

        //step 1: external service returns 6 random numbers (1,2,3,4,5,6)

        //given && when && then

        wireMockServer.stubFor(WireMock.get(EXTERNAL_SERVICE_ENDPOINT)
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(CONTENT_TYPE, CONTENT_TYPE_VALUE)
                                .withBody(retrieveNumbers())));

        //  step 2: system generated winning numbers for draw date: 8.11.2025 12:00


        //given && when && then
        await()
                .atMost(Duration.ofSeconds(25))
                .pollInterval(Duration.ofSeconds(1))
                .until(() -> {
                    try {
                        return !numberGeneratorFacade.retrieveWinningNumbersByDate(drawDate).getWinningNumbers().isEmpty();


                    } catch (WinningNumbersNotFoundException | NumberOutOfRangeException e) {
                        return false;
                    }
                });


//        step 3: user tried to get JWT token by requesting POST /token with email=email@gmail.com, password=somePassword and system returned UNAUTHORIZED(401)

        //given && when

        ResultActions failedLoginRequest = postJson(TOKEN_ENDPOINT, retrieveSomeUserWithSomePassword());

        //then

        failedLoginRequest.andExpect(status().isUnauthorized())
                .andExpect(content().json(
                        """
                                  {
                                    "message" : "Bad Credentials",
                                    "status" : "UNAUTHORIZED"
                                   }
                                """.trim()
                ));



//        step 4: user made POST /inputNumbers with no jwt token and system returned FORBIDDEN 403

        //given && when

        ResultActions failedInputNumbersRequest  = postJson(INPUT_NUMBERS_ENDPOINT, retrieveInputNumbers());

        //then
        failedInputNumbersRequest.andExpect(status().isForbidden());


//        step 5: user made POST /register with email=email@gmail.com, password=somePassword and system registered user with status OK(200)


        //given && when

        ResultActions successRegisteredResult = postJson(REGISTER_ENDPOINT, retrieveSomeUserWithSomePassword());

        String successRegisteredResultJson = successRegisteredResult.andReturn().getResponse().getContentAsString();
        UserRegisterResponseDto registeredUserResponse = objectMapper.readValue(successRegisteredResultJson,
                UserRegisterResponseDto.class);


        //then
        successRegisteredResult.andExpect(status().isCreated());
        assertAll(
                ()-> assertThat(registeredUserResponse.id()).isNotNull(),
                () -> assertThat(registeredUserResponse.email()).isEqualTo("email@gmail.com"),
                () -> assertThat(registeredUserResponse.isRegistered()).isTrue()
        );

//        step 6: user tried to get JWT token by requesting POST /token with email:email@gmail.com, password=somePassword and system returned OK(200) and jwttoken=AAAA.BBBB.CCC

        //given && when

        ResultActions  successLoginRequest = postJson(TOKEN_ENDPOINT, retrieveSomeUserWithSomePassword());

        //then

        MvcResult successLoginResult = successLoginRequest.andExpect(status().isOk()).andReturn();
        String successLoginJson = successLoginResult.getResponse().getContentAsString();
        JwtResponseDto jwtResponseDto = objectMapper.readValue(successLoginJson, JwtResponseDto.class);
        String token = jwtResponseDto.token();

        assertAll(
                () -> assertThat(jwtResponseDto.email()).isEqualTo("email@gmail.com"),
                () -> assertThat(token).matches(Pattern.compile("^([A-Za-z0-9-_=]+\\.)+([A-Za-z0-9-_=])+\\.?$")));


        //        step 7: user made POST /inputNumbers with header “Authorization: Bearer AAAA.BBBB.CCC” with 6 numbers (1, 2, 3, 4, 5, 6) at 8-11-2025 10:00 and system returned OK(200) with message: “success” and Ticket (DrawDate:8.11.2025 12:00 (Saturday), TicketId: sampleTicketId)

        //given & when
        ResultActions perform = mockMvc.perform(post(INPUT_NUMBERS_ENDPOINT)
                        .header("Authorization", "Bearer " + token)
                .content(
                        retrieveInputNumbers()
                )
                .contentType(MediaType.APPLICATION_JSON));
        //then


        MvcResult mvcResult = perform.andExpect(status().isOk()).andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        InputNumbersResponseDto inputNumbersResponseDto = objectMapper.readValue(result, InputNumbersResponseDto.class);
        String ticketId = inputNumbersResponseDto.ticketDto().ticketId();



        assertAll(
                () ->    assertThat(inputNumbersResponseDto.ticketDto().drawDate()).isEqualTo(drawDate),
                () ->  assertThat(inputNumbersResponseDto.message()).isEqualTo("SUCCESS"),
                () -> assertThat(ticketId).isNotNull()
        );



        //        step 8 user make GET request /results/{notExistingId} with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned 404

        //given & when

        ResultActions performResultsWithNotExistingId = mockMvc.perform(get(RESULTS_ENDPOINT + "/" + "notExistingId")
                .header("Authorization", "Bearer " + token));

        //then
        performResultsWithNotExistingId.andExpect(status().isNotFound())
                .andExpect(content().json(
                        retrieveNotFoundResponse()
                ));


//        step 9: 3 days and 55 minutes passed, and it is 5 minute before draw (8.11.2025 11:55)

        // given && when && then

            clock.plusDaysAndMinutes(3, 55);
            log.info("Clock plus 3 Days and 55 Minutes " +  clock);

//        step 10: system generated result for TicketId: sampleTicketId with draw date 8.11.2025 12:00, and saved it with 6 hits

        // given
        Player player = Player.builder()
                .numberOfHits(6)
                .hash(ticketId)
                .isWinner(true)
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .drawDate(drawDate)
                .winningNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .build();

        playersRepository.save(player);

        log.info("Players repository size " +  playersRepository.findAll().size());

        //when && then
        await()
                .atMost(Duration.ofSeconds(25))
                .pollInterval(Duration.ofSeconds(1))
                .until(() -> {
                    try{
                        PlayerDto resultTicket = resultCheckerFacade.findPlayerByTicketId(ticketId);
                        return !resultTicket.numbers().isEmpty();
                    } catch (PlayerNotFoundException e){
                        return false;
                    }

                });


//        step 11: 6 minutes passed and it is 1 minute after the draw (8.11.2025 12:01)

        //given && when && then

        clock.plusMinutes(6);


        //  step 12: user made GET /results/sampleTicketId with no jwt token and system returned FORBIDDEN 403

        //given && when

        ResultActions failedGetResultsRequest = mockMvc.perform(get( RESULTS_ENDPOINT+ "/" + ticketId)
                .contentType(MediaType.APPLICATION_JSON));


        //then
        failedGetResultsRequest.andExpect(status().isForbidden());


//        step 13: user made GET /results/sampleTicketId with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned 200 (OK)

        //given && when

        MvcResult returnedWinningInfo = mockMvc.perform(get(RESULTS_ENDPOINT+ "/" + ticketId)
                .header("Authorization", "Bearer " + token)).andReturn();
        String winningInfoJson = returnedWinningInfo.getResponse().getContentAsString();
        ResultMessageDto resultMessageDto = objectMapper.readValue(winningInfoJson, ResultMessageDto.class);


        //then

        assertAll(
                () -> assertThat(resultMessageDto).isNotNull(),
                () -> assertThat(resultMessageDto.resultDto().drawDate()).isEqualTo(drawDate),
                () -> assertThat(resultMessageDto.resultDto().isWinner()).isTrue(),
                () -> assertThat(resultMessageDto.resultDto().hash()).isEqualTo(ticketId),
                () -> assertThat(resultMessageDto.resultDto().winningNumbers()).hasSize(6),
                () -> assertThat(resultMessageDto.resultDto().numbers()).isEqualTo(Set.of(1, 2, 3, 4, 5, 6)),
                () -> assertThat(resultMessageDto.message()).isEqualTo("You are a winner! Congratulations!")
                        );


    }

    private ResultActions postJson(String endpoint, String body) throws Exception {

        return mockMvc.perform(post(endpoint)
                        .content(body)
                .contentType(MediaType.APPLICATION_JSON));
    }
}
