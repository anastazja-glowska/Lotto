package com.lotto.domain.resultchecker;


import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Component
class WinnersEvaluator {

    private static final Integer MINIMAL_WON_NUMBERS = 3;

    List<Player> evaluateWinners(List<Ticket> allTickets, Set<Integer> winningNumbers){

        List<Player> playerList = allTickets.stream()
                .map(ticket -> {
                    Integer countedMatchesHits = countMatchesHits(ticket, winningNumbers);
                    return createPlayerResult(ticket, countedMatchesHits, winningNumbers);
                })
                .filter(player -> player.isWinner())
                .toList();

        return playerList;
    }



    private Integer countMatchesHits(Ticket ticket, Set<Integer> winningNumbers){
        long counted = ticket.numbersFromUser().stream()
                .filter(winningNumbers::contains)
                .count();

        return (int)counted;


    }

    private Player createPlayerResult(Ticket ticket, Integer numberOfHits,  Set<Integer> winningNumbers){

        Player.PlayerBuilder player = Player.builder();

        if(numberOfHits >= MINIMAL_WON_NUMBERS){
            player.isWinner(true);
        }

        return player.hash(ticket.ticketId())
                .numbers(ticket.numbersFromUser())
                .drawDate(ticket.drawDate())
                .numberOfHits(numberOfHits)
                .winningNumbers(winningNumbers)
                .build();
    }
}
