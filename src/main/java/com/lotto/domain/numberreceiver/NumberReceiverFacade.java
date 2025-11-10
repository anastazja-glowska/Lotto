package com.lotto.domain.numberreceiver;

import com.lotto.domain.numberreceiver.dto.InputNumbersResponseDto;
import com.lotto.domain.numberreceiver.dto.TicketDto;
import lombok.RequiredArgsConstructor;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
public class NumberReceiverFacade {


    private final Clock clock;
    private final NumberChecker numberChecker;
    private final DrawDateGenerator drawDateGenerator;
    private final TicketRepository ticketRepository;

    public InputNumbersResponseDto inputNumbers(Set<Integer> numbersFromUser){
    return numberChecker.checkInputNumbers(numbersFromUser);

    }

    public List<TicketDto> retrieveAllTicketsByDrawDate(){
        return numberChecker.retrieveAllTicketsByDrawDate();
    }



    public List<TicketDto> retrieveAllTicketsByDrawDate(LocalDateTime drawDate){
        return numberChecker.retrieveAllTicketsByDrawDate(drawDate);
    }

    public LocalDateTime retrieveNextDrawnDate(){
        return drawDateGenerator.getNextDrawnDate();
    }

    public TicketDto findTicketByHash(String hash){
        Ticket ticket = ticketRepository.findTicketByHash(hash)
                .orElseThrow(() -> new TicketNotFoundException("Ticket not found"));
        return TicketMapper.mapFromTicket(ticket);
    }




}
