package com.lotto.domain.numberreceiver;

import com.lotto.domain.numberreceiver.dto.InputNumbersResultDto;
import com.lotto.domain.numberreceiver.dto.TicketDto;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
public class NumberReceiverFacade {

    private final NumberValidator numberValidator;
    private final NumberReceiverReposioty repository;

    public InputNumbersResultDto inputNumbers(Set<Integer> numbersFromUser){

        boolean numbersInRange = numberValidator.areAllNumbersInRange(numbersFromUser);


        if(numbersInRange){

            String ticketId = UUID.randomUUID().toString();
            LocalDateTime drawDate = LocalDateTime.now();

            Ticket savedTicket = repository.save(new Ticket(ticketId, drawDate, numbersFromUser));

            return InputNumbersResultDto
                    .builder()
                    .ticketId(savedTicket.ticketId())
                    .drawDate(drawDate)
                    .message("success")
                    .numbersFromUser(numbersFromUser)
                    .build();
        }
        return InputNumbersResultDto
                .builder()
                .message("failed")
                .build();


    }

    public List<TicketDto> userNumbers(LocalDateTime date){
        List<Ticket> allTicketsByDrawDate = repository.findAllTicketsByDrawDate(date);

    }


}
