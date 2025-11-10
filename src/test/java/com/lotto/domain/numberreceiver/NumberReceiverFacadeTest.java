package com.lotto.domain.numberreceiver;

import com.lotto.AdjustableClock;
import com.lotto.domain.numberreceiver.dto.InputNumbersResponseDto;
import com.lotto.domain.numberreceiver.dto.TicketDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class NumberReceiverFacadeTest {


    AdjustableClock clock = new AdjustableClock(LocalDateTime.of(2022, 6, 22, 11, 0, 0)
                    .toInstant(ZoneOffset.UTC), ZoneId.systemDefault());

    NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacade(
            new NumberValidator(),
            new InMemoryNumberReceiverRepository(),
            clock


    );

    @Test
    void should_return_success_when_user_gave_six_numbers(){

        //given 
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);
        //when
        
        InputNumbersResponseDto result = numberReceiverFacade.inputNumbers(numbersFromUser);

        //then
        assertThat(result.message()).isEqualTo("success");
    }

    @Test
    void should_return_failed_when_user_gave_less_than_six_numbers(){

        
        //given

        Set<Integer> numbersFromUser = Set.of(1, 3, 4, 5, 6);
        //when

        InputNumbersResponseDto result = numberReceiverFacade.inputNumbers(numbersFromUser);


        //then
        assertThat(result.message()).isEqualTo("failed");

    }

    @Test
    void should_return_failed_when_user_gave_more_than_six_numbers(){

        //given

        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6, 7);
        //when

        InputNumbersResponseDto result = numberReceiverFacade.inputNumbers(numbersFromUser);


        //then
        assertThat(result.message()).isEqualTo("failed");
    }

    @Test
    void should_return_failed_when_user_gave_at_least_one_number_out_of_range_1_to_99(){
        
        //given
        Set<Integer> numbersFromUser = Set.of(1, 200, 3, 4, 5, 6);

        //when

        InputNumbersResponseDto result = numberReceiverFacade.inputNumbers(numbersFromUser);


        //then
        assertThat(result.message()).isEqualTo("failed");
    }

    @Test
    void should_return_save_ticket_when_user_gave_six_numbers(){

        //given
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);
        InputNumbersResponseDto result = numberReceiverFacade.inputNumbers(numbersFromUser);

        LocalDateTime date = LocalDateTime.of(2022, 6, 22, 11, 0, 0);
        //when
        List<TicketDto> ticketDtos = numberReceiverFacade.userNumbers(result.drawDate());

        //then
        assertThat(ticketDtos).contains(
        TicketDto.builder()
                .ticketId(result.ticketId())
                .drawDate(result.drawDate())
                .numbersFromUser(result.numbersFromUser())
                .build());
    }

}