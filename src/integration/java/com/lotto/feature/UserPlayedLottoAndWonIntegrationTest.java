package com.lotto.feature;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.lotto.BaseIntegrationTest;
import com.lotto.domain.numbergenerator.NumberGeneratorFacade;
import com.lotto.domain.numbergenerator.RandomNumbersGenerable;
import com.lotto.domain.numbergenerator.WinningNumberRepository;
import com.lotto.domain.numbergenerator.WinningNumbersNotFoundException;
import com.lotto.domain.numbergenerator.dto.SixRandomNumbersDto;
import com.lotto.domain.numbergenerator.dto.WinningNumbersDto;
import com.lotto.domain.numberreceiver.dto.InputNumbersResponseDto;
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

import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log4j2
class UserPlayedLottoAndWonIntegrationTest extends BaseIntegrationTest {


    @Autowired
    NumberGeneratorFacade numberGeneratorFacade;

    @Autowired
    WinningNumberRepository winningNumberRepository;

    @BeforeEach
    void cleanDb(){
        winningNumberRepository.deleteAll();
    }

    @Test
    @DisplayName("Should user win and system should generate winners")
    void should_user_win_and_system_should_generate_winners() throws Exception {

        //        step 1: external service returns 6 random numbers (1,2,3,4,5,6)

        //given




        wireMockServer.stubFor(WireMock.get("/api/v1.0/random?min=1&max=99&count=25")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                                .withBody("""
                                        1, 2, 3, 4, 5, 6, 82, 82, 83, 83, 86, 57, 10, 81, 53, 93, 50, 54, 31, 88, 15, 43, 79, 32, 43]
                                                                                 
                                        """.trim())));



        WinningNumbersDto winningNumbers = numberGeneratorFacade.generateWinningNumbers();
        LocalDateTime date = winningNumbers.getDate();
        log.info("WinningNumbers: " + winningNumbers.getDate());


        //        step 2: system generated winning numbers for draw date: 15.11.2025 12:00

        //given

        LocalDateTime drawDate = LocalDateTime.of(2025, 11, 15, 12, 0, 0);

        //when & then
        await()
                .atMost(Duration.ofSeconds(25))
                .pollInterval(Duration.ofSeconds(1))
                .until(() -> {
                    try {
                        return !numberGeneratorFacade.retrieveWinningNumbersByDate(date).getWinningNumbers().isEmpty();

                    } catch (WinningNumbersNotFoundException e) {
                        return false;
                    }
                });





//        step 3: user made POST /inputNumbers with 6 numbers (1, 2, 3, 4, 5, 6) at 16-11-2022 10:00 and system returned OK(200) with message: “success” and Ticket (DrawDate:19.11.2022 12:00 (Saturday), TicketId: sampleTicketId)

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
        MvcResult mvcResult = perform.andExpect(status().isOk()).andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        InputNumbersResponseDto inputNumbersResponseDto = objectMapper.readValue(result, InputNumbersResponseDto.class);


        assertAll(
                () ->    assertThat(inputNumbersResponseDto.ticketDto().drawDate()).isEqualTo(drawDate),
                () ->  assertThat(inputNumbersResponseDto.message()).isEqualTo("SUCCESS"),
                () -> assertThat(inputNumbersResponseDto.ticketDto().ticketId()).isNotNull()
        );






//        step 1.5 (what if user makes GET /results/sampleTicketId see step 6)
//        step 4: 3 days, 2hrs and 1 minute passed, and it is 1 minute after the draw date (19.11.2022 12:01)
//        step 5: system generated result for TicketId: sampleTicketId with draw date 19.11.2022 12:00, and saved it with 6 hits
//        step 6: 3 hours passed, and it is 1 minute after announcement time (19.11.2022 15:01)
//        step 7: user made GET /results/sampleTicketId and system returned 200 (OK)

    }
}
