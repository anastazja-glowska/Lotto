package com.lotto.domain.numberreceiver;

import com.lotto.AdjustableClock;
import com.lotto.domain.numberreceiver.dto.InputNumbersResponseDto;
import com.lotto.domain.numberreceiver.dto.TicketDto;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@Log4j2
class NumberReceiverFacadeTest {


    private final TicketRepository ticketRepository = new TicketRepositoryTestImpl();
    private final HashGenerable hashGenerator = new HashGenerableTestImpl();
    Clock clock = Clock.systemUTC();


    private final NumberReceiverFacade numberReceiverFacade =
            new NumberReceiverConfiguration().numberReceiverFacade(clock, ticketRepository, hashGenerator);


    @Test
    @DisplayName("Should return correct response when user gave six numbers")
    void should_return_correct_response_when_user_gave_six_numbers() {

        //given 
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);
        DrawDateGenerator drawDateGenerator = new DrawDateGenerator(clock);
        LocalDateTime nextDrawnDate = drawDateGenerator.getNextDrawnDate();


        //when

        InputNumbersResponseDto result = numberReceiverFacade.inputNumbers(numbersFromUser);

        //then

        TicketDto expectedticketDto = TicketDto.builder()
                .drawDate(nextDrawnDate)
                .numbersFromUser(numbersFromUser)
                .ticketId(result.ticketDto().ticketId())
                .build();


        InputNumbersResponseDto expectedResponse =
                new InputNumbersResponseDto(ValidationInfo.SUCCESS_MESSAGE.message, expectedticketDto);

        assertThat(result).isEqualTo(expectedResponse);
        assertThat(result.message()).isEqualTo(expectedResponse.message());
    }

    @Test
    @DisplayName("Should return failed answer when user gave less than six numbers")
    void should_return_failed_answer_when_user_gave_less_than_six_numbers() {


        //given

        Set<Integer> numbersFromUser = Set.of(1, 3, 4, 5, 6);
        //when

        InputNumbersResponseDto result = numberReceiverFacade.inputNumbers(numbersFromUser);


        //then
        InputNumbersResponseDto expectedResult =
                new InputNumbersResponseDto(ValidationInfo.SIX_NUMBERS_ARE_NOT_PROVIDEN.message, null);

        assertThat(result.message()).contains(expectedResult.message());

    }

    @Test
    @DisplayName("Should return failed when user gave more than six numbers")
    void should_return_failed_when_user_gave_more_than_six_numbers() {

        //given

        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6, 7);
        //when

        InputNumbersResponseDto result = numberReceiverFacade.inputNumbers(numbersFromUser);


        //then
        InputNumbersResponseDto expectedResult =
                new InputNumbersResponseDto(ValidationInfo.SIX_NUMBERS_ARE_NOT_PROVIDEN.message, null);

        assertThat(result.message()).contains(expectedResult.message());
    }

    @Test
    @DisplayName("Should return failed when user gave at least one number out of range 1 to 99")
    void should_return_failed_when_user_gave_at_least_one_number_out_of_range_1_to_99() {

        //given
        Set<Integer> numbersFromUser = Set.of(1, 200, 3, 4, 5, 6);

        //when

        InputNumbersResponseDto result = numberReceiverFacade.inputNumbers(numbersFromUser);


        //then
        InputNumbersResponseDto expectedResult =
                new InputNumbersResponseDto(ValidationInfo.NUMBERS_NOT_IN_RANGE.message, null);

        assertThat(result.message()).contains(expectedResult.message());
    }

    @Test
    @DisplayName("Should return ticket by hash")
    void should_return_ticket_by_hash() {

        //given
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);
        String hash = numberReceiverFacade.inputNumbers(numbersFromUser).ticketDto().ticketId();

        //when

        TicketDto ticketByHash = numberReceiverFacade.findTicketByHash(hash);

        //then
        assertThat(ticketByHash.ticketId()).isEqualTo(hash);

    }


    @Test
    @DisplayName("Should return next Saturday draw date when date is Saturday noon")
    void should_return_next_Saturday_draw_date_when_date_is_Saturday_noon() {

        //given
        Clock fixedClock = Clock.fixed(LocalDateTime.of(2025, 10, 18, 12, 0, 0)
                .toInstant(ZoneOffset.UTC), ZoneId.of(("Europe/London")));

        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);
        NumberReceiverFacade facade = new NumberReceiverConfiguration()
                .numberReceiverFacade(fixedClock, ticketRepository, hashGenerator);

        facade.inputNumbers(numbersFromUser);

        //when

        LocalDateTime drawnDate = facade.retrieveNextDrawnDate();


        //then
        LocalDateTime expectedDrawnDate = LocalDateTime.of(2025, 10, 25, 12, 0, 0);

        assertThat(drawnDate).isEqualTo(expectedDrawnDate);


    }

    @Test
    @DisplayName("Should return correct hash")
    void should_return_correct_hash() {

        //given

        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);

        HashGenerable newHashGenerator = new HashGenerator();
        NumberReceiverFacade facade = new NumberReceiverConfiguration()
                .numberReceiverFacade(clock, ticketRepository, newHashGenerator);


        //when
        String hash = facade.inputNumbers(numbersFromUser).ticketDto().ticketId();


        //then
        assertThat(hash).isNotNull();
        assertThat(hash).isNotEmpty();
        assertThat(hash).hasSize(36);
    }


    @Test
    @DisplayName("Should return correct draw date")
    void should_return_correct_draw_date() {
        //given
        Clock fixedClock = Clock.fixed(LocalDateTime.of(2025, 11, 8, 10, 0, 0)
                .toInstant(ZoneOffset.UTC), ZoneId.of(("Europe/London")));

        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);
        NumberReceiverFacade facade = new NumberReceiverConfiguration()
                .numberReceiverFacade(fixedClock, ticketRepository, hashGenerator);


        //when
        LocalDateTime drawnDate = facade.inputNumbers(numbersFromUser).ticketDto().drawDate();


        //then
        LocalDateTime expectedDrawnDate = LocalDateTime.of(2025, 11, 8, 12, 0, 0);
        assertThat(drawnDate).isEqualTo(expectedDrawnDate);
    }

    @Test
    @DisplayName("Should return correct tickets by draw date")
    void should_return_correct_tickets_by_draw_date() {

        //given


        Instant instant = LocalDateTime.of(2025, 11, 11, 12, 0, 0)
                .toInstant(ZoneOffset.UTC);

        ZoneId zoneId = ZoneId.of(("Europe/London"));


        AdjustableClock fixedClock = new AdjustableClock(instant, zoneId);

        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);
        NumberReceiverFacade facade = new NumberReceiverConfiguration()
                .numberReceiverFacade(fixedClock, ticketRepository, hashGenerator);

        InputNumbersResponseDto responseDto = facade.inputNumbers(numbersFromUser);
        InputNumbersResponseDto responseDto1 = facade.inputNumbers(Set.of(1, 2, 3, 8, 5, 6));
        fixedClock.plusDays(7);
        InputNumbersResponseDto responseDto2 = facade.inputNumbers(Set.of(1, 2, 3, 8, 5, 9));

        Instant instantAfter = LocalDateTime.of(2025, 11, 11, 12, 0, 0)
                .toInstant(ZoneOffset.UTC);

        fixedClock = new AdjustableClock(instantAfter, zoneId);

        facade = new NumberReceiverConfiguration().numberReceiverFacade(fixedClock, ticketRepository, hashGenerator);
        log.info("Fixed clock " + fixedClock.toString());

        log.info("Draw date 1: " + responseDto.ticketDto().drawDate());
        log.info("Draw date 2: " + responseDto1.ticketDto().drawDate());
        log.info("Draw date 3: " + responseDto2.ticketDto().drawDate());


        //when
        List<TicketDto> ticketDtos = facade.retrieveAllTicketsByDrawDate(responseDto.ticketDto().drawDate());

        log.info("Tickets dtos size " + ticketDtos.size());

        //then
        assertThat(ticketDtos).hasSize(2);
        assertThat(ticketDtos).containsOnly(responseDto.ticketDto(), responseDto1.ticketDto());

    }

    @Test
    @DisplayName("Should return saturday draw date when it is monday")
    void should_return_saturday_draw_date_when_it_is_monday() {


        //given
        Clock fixedClock = Clock.fixed(LocalDateTime.of(2025, 11, 10, 12, 0, 0)
                .toInstant(ZoneOffset.UTC), ZoneId.of(("Europe/London")));

        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);
        NumberReceiverFacade facade = new NumberReceiverConfiguration()
                .numberReceiverFacade(fixedClock, ticketRepository, hashGenerator);


        //when

        LocalDateTime drawnDate = facade.inputNumbers(numbersFromUser).ticketDto().drawDate();


        //then
        LocalDateTime expectedDrawnDate = LocalDateTime.of(2025, 11, 15, 12, 0, 0);

        assertThat(drawnDate).isEqualTo(expectedDrawnDate);
    }

    @Test
    @DisplayName("Should return empty list when there is no tickets")
    void should_return_empty_list_when_there_is_no_tickets() {

        //given


        Instant instant = LocalDateTime.of(2025, 11, 11, 12, 0, 0)
                .toInstant(ZoneOffset.UTC);

        ZoneId zoneId = ZoneId.of(("Europe/London"));


        AdjustableClock fixedClock = new AdjustableClock(instant, zoneId);

        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);
        NumberReceiverFacade facade = new NumberReceiverConfiguration()
                .numberReceiverFacade(fixedClock, ticketRepository, hashGenerator);

        InputNumbersResponseDto responseDto = facade.inputNumbers(numbersFromUser);


        //when
        List<TicketDto> ticketDtos = facade.retrieveAllTicketsByDrawDate(responseDto
                .ticketDto().drawDate().plusDays(7));

        //then

        assertThat(ticketDtos).isEmpty();
    }


    @Test
    @DisplayName("Should throw exception when ticket not found")
    void should_throw_exception_when_ticket_not_found() {


        //when

        Throwable throwable = catchThrowable(() -> numberReceiverFacade.findTicketByHash("hash"));

        //then
        assertThat(throwable).isInstanceOf(TicketNotFoundException.class);


    }
}