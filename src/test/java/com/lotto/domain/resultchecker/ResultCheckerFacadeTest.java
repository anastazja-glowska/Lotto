package com.lotto.domain.resultchecker;

import com.lotto.domain.numbergenerator.NumberGeneratorFacade;
import com.lotto.domain.numbergenerator.dto.WinningNumbersDto;
import com.lotto.domain.numberreceiver.NumberReceiverFacade;
import com.lotto.domain.numberreceiver.dto.TicketDto;
import com.lotto.domain.resultchecker.dto.AllPlayersDto;
import com.lotto.domain.resultchecker.dto.PlayerDto;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@Log4j2
class ResultCheckerFacadeTest {


    NumberReceiverFacade numberReceiverFacade = mock(NumberReceiverFacade.class);
    NumberGeneratorFacade numberGeneratorFacade = mock(NumberGeneratorFacade.class);
    PlayersRepository playersRepository = new PlayersRepositoryTestImpl();

    WinnersEvaluator winnersEvaluator = new WinnersEvaluator();
    ResultRetriever resultRetriever = new ResultRetriever(numberReceiverFacade, numberGeneratorFacade,
            winnersEvaluator, playersRepository);

    ResultCheckerFacade resultCheckerFacade = new ResultCheckerConfiguration().resultCheckerFacade(resultRetriever);

    @Test
    @DisplayName("Should retrieve correct winners")
    void should_retrieve_correct_winners(){

        //given

        LocalDateTime drawDate = LocalDateTime.of(2025, 11, 15, 12, 0, 0);


        when(numberReceiverFacade.retrieveAllTicketsByDrawDate()).thenReturn(List.of(
                TicketDto.builder()
                        .numbersFromUser(Set.of(1, 2, 3, 4, 5, 6))
                        .drawDate(drawDate)
                        .ticketId("001")
                        .build(),
                TicketDto.builder()
                        .numbersFromUser(Set.of(1, 22, 33, 4, 5, 6))
                        .drawDate(drawDate)
                        .ticketId("002")
                        .build(),
                TicketDto.builder()
                        .numbersFromUser(Set.of(11, 22, 3, 4, 5, 6))
                        .drawDate(drawDate)
                        .ticketId("003")
                        .build()

                ));

        when(numberGeneratorFacade.generateWinningNumbers()).thenReturn(WinningNumbersDto.builder()
                        .winningNumbers(Set.of(1, 2, 3, 4, 5, 6))
                        .date(drawDate)
                .build());


        //when
        AllPlayersDto result = resultCheckerFacade.retrieveWinners();
        List<PlayerDto> players = result.players();

        //then
        PlayerDto playerDto1 = PlayerDto.builder()
                .isWinner(true)
                .hash("001")
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .winningNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .drawDate(drawDate)
                .build();

        PlayerDto playerDto2 = PlayerDto.builder()
                .isWinner(true)
                .hash("002")
                .numbers(Set.of(1, 22, 33, 4, 5, 6))
                .winningNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .drawDate(drawDate)
                .build();

        PlayerDto playerDto3 = PlayerDto.builder()
                .isWinner(true)
                .hash("003")
                .numbers(Set.of(11, 22, 3, 4, 5, 6))
                .winningNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .drawDate(drawDate)
                .build();

        assertThat(players).contains(playerDto1, playerDto2, playerDto3);
        assertThat(result).isNotNull();
        assertThat(players).hasSize(3);


    }




    @Test
    @DisplayName("Should retrieve only winners with min three winning numbers")
    void should_retrieve_only_winners_with_min_three_winning_numbers(){

        //given

        LocalDateTime drawDate = LocalDateTime.of(2025, 11, 15, 12, 0, 0);

        when(numberReceiverFacade.retrieveAllTicketsByDrawDate()).thenReturn(List.of(
                TicketDto.builder()
                        .numbersFromUser(Set.of(1, 2, 3, 4, 5, 6))
                        .drawDate(drawDate)
                        .ticketId("001")
                        .build(),
                TicketDto.builder()
                        .numbersFromUser(Set.of(1, 22, 33, 44, 55, 6))
                        .drawDate(drawDate)
                        .ticketId("002")
                        .build(),
                TicketDto.builder()
                        .numbersFromUser(Set.of(11, 22, 3, 4, 5, 6))
                        .drawDate(drawDate)
                        .ticketId("003")
                        .build()

        ));

        when(numberGeneratorFacade.generateWinningNumbers()).thenReturn(WinningNumbersDto.builder()
                .winningNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .date(drawDate)
                .build());


        //when
        AllPlayersDto result = resultCheckerFacade.retrieveWinners();
        List<PlayerDto> players = result.players();

        //then

        log.info("Players size :" + players.size());
        log.info("Result " +  result) ;

        PlayerDto playerDto1 = PlayerDto.builder()
                .isWinner(true)
                .hash("001")
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .winningNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .drawDate(drawDate)
                .build();

        PlayerDto playerDto2 = PlayerDto.builder()
                .isWinner(false)
                .hash("002")
                .numbers(Set.of(1, 22, 33, 44, 55, 6))
                .winningNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .drawDate(drawDate)
                .build();

        PlayerDto playerDto3 = PlayerDto.builder()
                .isWinner(true)
                .hash("003")
                .numbers(Set.of(11, 22, 3, 4, 5, 6))
                .winningNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .drawDate(drawDate)
                .build();

        assertThat(players).containsOnly(playerDto1, playerDto3);
        assertThat(result).isNotNull();
        assertThat(players).hasSize(2);


    }

    @Test
    @DisplayName("Should return any players when winning numbers are null")
    void should_return_any_players_when_winning_numbers_are_null(){
        // given


        LocalDateTime drawDate = LocalDateTime.of(2025, 11, 15, 12, 0, 0);

        when(numberReceiverFacade.retrieveAllTicketsByDrawDate()).thenReturn(List.of(
                TicketDto.builder()
                        .numbersFromUser(Set.of(1, 2, 3, 4, 5, 6))
                        .drawDate(drawDate)
                        .ticketId("001")
                        .build(),
                TicketDto.builder()
                        .numbersFromUser(Set.of(1, 22, 33, 44, 55, 6))
                        .drawDate(drawDate)
                        .ticketId("002")
                        .build(),
                TicketDto.builder()
                        .numbersFromUser(Set.of(11, 22, 3, 4, 5, 6))
                        .drawDate(drawDate)
                        .ticketId("003")
                        .build()

        ));

        when(numberGeneratorFacade.generateWinningNumbers()).thenReturn(WinningNumbersDto.builder()
                .winningNumbers(null)
                .date(drawDate)
                .build());


        //when
        AllPlayersDto result = resultCheckerFacade.retrieveWinners();
        List<PlayerDto> players = result.players();


        //then
        assertThat(players).isNull();
    }

    @Test
    @DisplayName("Should find player by given ticket id")
    void should_find_player_by_given_ticket_id(){
        //given

        LocalDateTime drawDate = LocalDateTime.of(2025, 11, 15, 12, 0, 0);

        Player player1 = Player.builder()
                .isWinner(true)
                .hash("001")
                .numbers(Set.of(1, 22, 33, 44, 5, 6))
                .winningNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .drawDate(drawDate)
                .numberOfHits(3)
                .build();

        Player player2 = Player.builder()
                .isWinner(true)
                .hash("002")
                .numbers(Set.of(1,2, 3, 4, 5, 6))
                .winningNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .drawDate(drawDate)
                .numberOfHits(6)
                .build();

        playersRepository.saveAll(List.of(player1, player2));



        //when
        PlayerDto result = resultCheckerFacade.findPlayerByTicketId("002");

        //then

        PlayerDto playerDto2 = PlayerDto.builder()
                .isWinner(false)
                .hash("002")
                .numbers(Set.of(1, 22, 33, 44, 55, 6))
                .winningNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .drawDate(drawDate)
                .build();

        assertThat(result).isNotNull();
        assertThat(result.hash()).isEqualTo(playerDto2.hash());
        assertThat(result.drawDate()).isEqualTo(playerDto2.drawDate());
        assertThat(result.isWinner()).isEqualTo(player2.isWinner());
    }

    @Test
    @DisplayName("Should throw player not found exception when player not found")
    void should_throw_player_not_found_exception_when_player_not_found(){

        //given

        LocalDateTime drawDate = LocalDateTime.of(2025, 11, 15, 12, 0, 0);

        Player player1 = Player.builder()
                .isWinner(true)
                .hash("001")
                .numbers(Set.of(1, 22, 33, 44, 5, 6))
                .winningNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .drawDate(drawDate)
                .numberOfHits(3)
                .build();

        //when

        Throwable throwable = catchThrowable(() -> resultCheckerFacade.findPlayerByTicketId("002"));

        //then
        assertThat(throwable).isInstanceOf(PlayerNotFoundException.class);
    }

}