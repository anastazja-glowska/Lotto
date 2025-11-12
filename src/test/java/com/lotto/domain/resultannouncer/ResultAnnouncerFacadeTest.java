package com.lotto.domain.resultannouncer;

import com.lotto.domain.resultannouncer.dto.ResultDto;
import com.lotto.domain.resultannouncer.dto.ResultMessageDto;
import com.lotto.domain.resultchecker.ResultCheckerFacade;
import com.lotto.domain.resultchecker.dto.PlayerDto;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@Log4j2
class ResultAnnouncerFacadeTest {

    ResultRepository resultRepository = new ResultRepositoryTestImpl();
    Clock clock = Clock.systemUTC();
    ResultCheckerFacade resultCheckerFacade = Mockito.mock(ResultCheckerFacade.class);
    ResultAnnounceRetriever resultAnnounceRetriever = new ResultAnnounceRetriever(resultRepository,
            resultCheckerFacade, clock);

    ResultAnnouncerFacade resultAnnouncerFacade = new ResultAnnouncerConfiguration()
            .resultAnnouncerFacade(resultAnnounceRetriever);


    @Test
    @DisplayName("Should return already checked info when result repository found by id")
    void should_return_already_checked_info_when_result_repository_found_by_id(){
        LocalDateTime drawDate = LocalDateTime.of(2025, 11, 8, 12, 0, 0);

        //given
        ResultAnswer resultAnswer = ResultAnswer.builder()
                .hash("001")
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .winningNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .drawDate(drawDate)
                .isWinner(true)
                .build();

        resultRepository.save(resultAnswer);

        //when
        ResultMessageDto result = resultAnnouncerFacade.checkPlayResultByHash("001");

        //then
        String message = result.message();
        log.info("Message " +  message);
        ResultDto resultDto = result.resultDto();

        ResultDto expectedResultDto = ResultDto.builder()
                .hash("001")
                .isWinner(true)
                .winningNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .drawDate(drawDate)
                .build();


        assertThat(message).isEqualTo(MessageAnswer.ALREADY_CHECKED_INFO.message);
        assertThat(resultDto.drawDate()).isEqualTo(expectedResultDto.drawDate());
        assertThat(resultDto.isWinner()).isEqualTo(expectedResultDto.isWinner());
        assertThat(resultDto.winningNumbers()).isEqualTo(expectedResultDto.winningNumbers());

    }

    @Test
    @DisplayName("Should return hash not exist message when ticket id is null")
    void should_return_hash_not_exist_message_when_ticket_id_is_null(){
        // given
        when(resultCheckerFacade.findPlayerByTicketId("001")).thenReturn(null);

        //when
        ResultMessageDto result = resultAnnouncerFacade.checkPlayResultByHash("001");

        //then
        String resultMessage = result.message();

        ResultDto expectedResultDto = ResultDto.builder()
                .hash(null)
                .isWinner(false)
                .winningNumbers(null)
                .numbers(null)
                .drawDate(null)
                .build();

        assertThat(resultMessage).isEqualTo(MessageAnswer.HASH_DOES_NOT_EXIST.message);
        assertThat(result.resultDto()).isEqualTo(expectedResultDto);
    }

    @Test
    @DisplayName("Should return wait info when draw date is not passed")
    void should_return_wait_info_when_draw_date_is_not_passed(){

        //given

        Clock fixed = Clock.fixed(LocalDateTime.of(2025, 11, 11, 12, 0, 0)
                .toInstant(ZoneOffset.UTC), ZoneId.systemDefault());

        LocalDateTime drawDate = LocalDateTime.of(2025, 11, 15, 12, 0, 0);



        ResultAnnounceRetriever resultAnnounceRetriever1 = new ResultAnnounceRetriever(resultRepository,
                resultCheckerFacade, fixed);

        ResultAnnouncerFacade resultAnnouncerFacade1 = new ResultAnnouncerConfiguration()
                .resultAnnouncerFacade(resultAnnounceRetriever1);


        ResultAnswer resultAnswer = ResultAnswer.builder()
                .hash("001")
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .winningNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .drawDate(drawDate)
                .isWinner(true)
                .build();

        PlayerDto playerDto = PlayerDto.builder()
                .winningNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .drawDate(drawDate)
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .hash("001")
                .isWinner(true)
                .build();


        when(resultCheckerFacade.findPlayerByTicketId("001")).thenReturn(playerDto);

        resultRepository.save(resultAnswer);

        //when
        ResultMessageDto result = resultAnnouncerFacade1.checkPlayResultByHash("001");


        //then
        String message = result.message();
        log.info("Message " +  message);
        ResultDto resultDto = result.resultDto();

        ResultDto expectedResultDto = ResultDto.builder()
                .hash("001")
                .isWinner(true)
                .winningNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .drawDate(drawDate)
                .build();

        assertThat(message).isEqualTo(MessageAnswer.WAIT_INFO.message);
        assertThat(resultDto.drawDate()).isEqualTo(expectedResultDto.drawDate());
        assertThat(resultDto.isWinner()).isEqualTo(expectedResultDto.isWinner());
        assertThat(resultDto.winningNumbers()).isEqualTo(expectedResultDto.winningNumbers());
        assertThat(resultDto.numbers()).isEqualTo(expectedResultDto.numbers());



    }

    @Test
    @DisplayName("Should return win info when given numbers are winning numbers")
    void should_return_win_info_when_given_numbers_are_winning_numbers(){

        // given

        LocalDateTime drawDate = LocalDateTime.of(2025, 11, 8, 12, 0, 0);

        PlayerDto playerDto = PlayerDto.builder()
                .winningNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .drawDate(drawDate)
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .hash("001")
                .isWinner(true)
                .build();

        when(resultCheckerFacade.findPlayerByTicketId("001")).thenReturn(playerDto);



        //when
        ResultMessageDto result = resultAnnouncerFacade.checkPlayResultByHash(playerDto.hash());

        //then

        String resultMessage = result.message();
        log.info("Message " +  resultMessage);
        assertThat(resultMessage).isEqualTo(MessageAnswer.WIN_INFO.message);
    }



    @Test
    @DisplayName("Should return lose info when given numbers are not winning numbers")
    void should_return_lose_info_when_given_numbers_are_not_winning_numbers(){

        // given

        LocalDateTime drawDate = LocalDateTime.of(2025, 11, 8, 12, 0, 0);

        PlayerDto playerDto = PlayerDto.builder()
                .winningNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .drawDate(drawDate)
                .numbers(Set.of(1, 22, 33, 44, 55, 6))
                .hash("001")
                .isWinner(false)
                .build();

        when(resultCheckerFacade.findPlayerByTicketId("001")).thenReturn(playerDto);


        //when
        ResultMessageDto result = resultAnnouncerFacade.checkPlayResultByHash(playerDto.hash());

        //then

        String resultMessage = result.message();
        log.info("Message " +  resultMessage);
        assertThat(resultMessage).isEqualTo(MessageAnswer.LOSE_INFO.message);
    }
}