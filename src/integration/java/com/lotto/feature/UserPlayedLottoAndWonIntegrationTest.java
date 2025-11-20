package com.lotto.feature;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.lotto.BaseIntegrationTest;
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
import com.lotto.infrastructure.numbergenerator.scheduler.WinningNumberScheduler;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;

import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log4j2
class UserPlayedLottoAndWonIntegrationTest extends BaseIntegrationTest {


    @Autowired
    NumberGeneratorFacade numberGeneratorFacade;

    @Autowired
    WinningNumberRepository winningNumberRepository;

    @Autowired
    ResultCheckerFacade resultCheckerFacade;

    @Autowired
    PlayersRepository playersRepository;



    private LocalDateTime drawDate = LocalDateTime.of(2025, 11, 8, 12, 0, 0);

    @BeforeEach
    void setup() {
        winningNumberRepository.deleteAll();

        WinningNumbers prepared = WinningNumbers.builder()
                .date(drawDate)
                .winningNumbers(Set.of(1,2,3,4,5,6))
                .build();


        winningNumberRepository.save(prepared);
    }



    @Test
    @DisplayName("Should user win and system should generate winners")
    void should_user_win_and_system_should_generate_winners() throws Exception {

        //        step 1: external service returns 6 random numbers (1,2,3,4,5,6)

        //given && when && then

        wireMockServer.stubFor(WireMock.get("/api/v1.0/random?min=1&max=99&count=6")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                                .withBody("""
                                        [1, 2, 3, 4, 5, 6]
                                                                                 
                                        """.trim())));



        //        step 2: system generated winning numbers for draw date: 15.11.2025 12:00

        //given

       

        //when & then
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



        //        step 3: user made POST /inputNumbers with 6 numbers (1, 2, 3, 4, 5, 6) at 8-11-2025 10:00 and system returned OK(200) with message: “success” and Ticket (DrawDate:8.11.2025 12:00 (Saturday), TicketId: sampleTicketId)

        //given & when
        ResultActions perform = mockMvc.perform(post("/inputNumbers")
                .content(
                        """
                                {
                                "inputNumbers" : [1, 2, 3, 4 ,5 ,6 ]
                                }
                                """
                )
                .contentType(MediaType.APPLICATION_JSON));
        //then

        log.info("Winning numbers repository size " +  winningNumberRepository.findAll().size());
        MvcResult mvcResult = perform.andExpect(status().isOk()).andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        InputNumbersResponseDto inputNumbersResponseDto = objectMapper.readValue(result, InputNumbersResponseDto.class);
        String ticketId = inputNumbersResponseDto.ticketDto().ticketId();

        log.info("Ticket id " +  ticketId);


        assertAll(
                () ->    assertThat(inputNumbersResponseDto.ticketDto().drawDate()).isEqualTo(drawDate),
                () ->  assertThat(inputNumbersResponseDto.message()).isEqualTo("SUCCESS"),
                () -> assertThat(ticketId).isNotNull()
        );






//        step 4 user make GET request /results/{notExistingId} and system returned 404

        //given & when
        ResultActions performResultsWithNotExistingId = mockMvc.perform(get("/results/" + "notExistingId"));
        //then
        performResultsWithNotExistingId.andExpect(status().isNotFound())
                .andExpect(content().json(
                        """
                               {
                               "message" : "Player not found for id notExistingId",
                               "status" : "NOT_FOUND"
                               }
                               """.trim()
                ));

//        step 5: 3 days and 55 minutes passed, and it is 5 minute before draw (8.11.2025 11:55)

        // given && when && then
            clock.plusDaysAndMinutes(3, 55);
            log.info("Clock " +  clock);

//        step 6: system generated result for TicketId: sampleTicketId with draw date 8.11.2025 12:00, and saved it with 6 hits

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


//        step 7: 6 minutes passed and it is 1 minute after the draw (8.11.2025 12:01)
        //given && when && then
        clock.plusMinutes(6);
//        step 8: user made GET /results/sampleTicketId and system returned 200 (OK)

        //given && when
        MvcResult returnedWinningInfo = mockMvc.perform(get("/results/" + ticketId)).andReturn();
        String winningInfoJson = returnedWinningInfo.getResponse().getContentAsString();
        ResultMessageDto resultMessageDto = objectMapper.readValue(winningInfoJson, ResultMessageDto.class);

        log.info(resultMessageDto);

        //then

        assertAll(
                () -> assertThat(resultMessageDto).isNotNull()

                        );


    }
}
