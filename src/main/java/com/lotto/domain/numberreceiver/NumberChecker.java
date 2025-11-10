package com.lotto.domain.numberreceiver;

import com.lotto.domain.numberreceiver.dto.InputNumbersResponseDto;
import com.lotto.domain.numberreceiver.dto.TicketDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Log4j2
class NumberChecker {

    private final NumberValidator numberValidator;
    private final DrawDateGenerator drawDateGenerator;
    private final HashGenerable hashGenerator;
    private final TicketRepository ticketRepository;


    InputNumbersResponseDto checkInputNumbers(Set<Integer> userNumbers) {
        List<ValidationInfo> validationInfos = numberValidator.validateUserNumbers(userNumbers);

        if (!validationInfos.isEmpty()) {
            String resultMessages = numberValidator.allResultMessage();
            return new InputNumbersResponseDto(resultMessages, null);
        }

        LocalDateTime nextDrawnDate = drawDateGenerator.getNextDrawnDate();
        String ticketId = hashGenerator.getHash();

        Ticket ticket = saveTicket(userNumbers, nextDrawnDate, ticketId);

        TicketDto ticketDto = TicketMapper.mapFromTicket(ticket);

        return new InputNumbersResponseDto(ValidationInfo.SUCCESS_MESSAGE.message, ticketDto);


    }

    List<TicketDto> retrieveAllTicketsByDrawDate(){
        LocalDateTime nextDrawnDate = drawDateGenerator.getNextDrawnDate();
        return retrieveAllTicketsByDrawDate(nextDrawnDate);
    }

    List<TicketDto> retrieveAllTicketsByDrawDate(LocalDateTime drawDate){
        LocalDateTime nextDrawnDate = drawDateGenerator.getNextDrawnDate();
        log.info("Draw date in numberChecker " + nextDrawnDate);
        if(drawDate.isAfter(nextDrawnDate)){
            return new ArrayList<>();
        }

        List<TicketDto> list = ticketRepository.findAllTicketsByDrawDate(drawDate).stream()
                .filter(ticket -> ticket.drawDate().equals(drawDate))
                .map(TicketMapper::mapFromTicket)
                .toList();

        log.info("Tickets dtos size after lambda " + list.size());

        return list;
    }

    private Ticket saveTicket(Set<Integer> userNumbers, LocalDateTime nextDrawnDate, String ticketId) {
        Ticket ticket = Ticket.builder()
                .numbersFromUser(userNumbers)
                .drawDate(nextDrawnDate)
                .ticketId(ticketId)
                .build();

        return ticketRepository.save(ticket);

    }
}
