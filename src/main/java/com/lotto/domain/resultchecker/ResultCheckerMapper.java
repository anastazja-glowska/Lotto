package com.lotto.domain.resultchecker;

import com.lotto.domain.numberreceiver.dto.TicketDto;
import com.lotto.domain.resultchecker.dto.PlayerDto;

import java.util.List;

class ResultCheckerMapper {

    static List<Ticket> mapFromTicketDto(List<TicketDto> ticketDtoList) {

        return ticketDtoList.stream()
                .map(ticketDto -> Ticket.builder()
                        .drawDate(ticketDto.drawDate())
                        .numbersFromUser(ticketDto.numbersFromUser())
                        .ticketId(ticketDto.ticketId())
                        .build())
                .toList();

    }

    static List<PlayerDto> mapFromPlayers(List<Player> players) {

        return players.stream()
                .map(player -> PlayerDto.builder()
                        .hash(player.hash())
                        .isWinner(player.isWinner())
                        .numbers(player.numbers())
                        .winningNumbers(player.winningNumbers())
                        .drawDate(player.drawDate()).build())
                .toList();
    }

    static PlayerDto mapFromPlayer(Player player) {
        return PlayerDto.builder()
                .numbers(player.numbers())
                .hash(player.hash())
                .winningNumbers(player.winningNumbers())
                .drawDate(player.drawDate())
                .isWinner(player.isWinner())
                .build();
    }


}
