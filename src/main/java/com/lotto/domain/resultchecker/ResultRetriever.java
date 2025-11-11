package com.lotto.domain.resultchecker;

import com.lotto.domain.numbergenerator.NumberGeneratorFacade;
import com.lotto.domain.numbergenerator.dto.WinningNumbersDto;
import com.lotto.domain.numberreceiver.NumberReceiverFacade;
import com.lotto.domain.numberreceiver.dto.TicketDto;
import com.lotto.domain.resultchecker.dto.AllPlayersDto;
import com.lotto.domain.resultchecker.dto.PlayerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Log4j2
class ResultRetriever {

    private final NumberReceiverFacade numberReceiverFacade;
    private final NumberGeneratorFacade numberGeneratorFacade;
    private final WinnersEvaluator winnersEvaluator;
    private final PlayersRepository playersRepository;

    AllPlayersDto retrieveWinners(){
        List<TicketDto> tickets = numberReceiverFacade.retrieveAllTicketsByDrawDate();
        List<Ticket> ticketList = ResultCheckerMapper.mapFromTicketDto(tickets);
        WinningNumbersDto winningNumbersDto = numberGeneratorFacade.generateWinningNumbers();
        Set<Integer> winningNumbers = winningNumbersDto.getWinningNumbers();
        if(winningNumbers.isEmpty() || winningNumbers == null){
            log.info("No winning numbers found");
            return AllPlayersDto.builder().build();
        }

        List<Player> players = winnersEvaluator.evaluateWinners(ticketList, winningNumbers);
        List<PlayerDto> playerDtos = ResultCheckerMapper.mapFromPlayers(players);
        playersRepository.saveAll(players);

        return AllPlayersDto.builder()
                .players(playerDtos)
                .build();


    }

    PlayerDto findPlayerByTicketId(String ticketId){
        Player player = playersRepository.findById(ticketId)
                .orElseThrow(() -> new PlayerNotFoundException("Player not found!"));

        return ResultCheckerMapper.mapFromPlayer(player);
    }
}
