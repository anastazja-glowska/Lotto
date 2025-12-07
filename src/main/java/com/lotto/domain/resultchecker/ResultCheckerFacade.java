package com.lotto.domain.resultchecker;


import com.lotto.domain.resultchecker.dto.AllPlayersDto;
import com.lotto.domain.resultchecker.dto.PlayerDto;
import jdk.jfr.Registered;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ResultCheckerFacade {

    private final ResultRetriever resultRetriever;

    public AllPlayersDto retrieveWinners() {
        return resultRetriever.retrieveWinners();
    }

    public PlayerDto findPlayerByTicketId(String ticketId) {
        return resultRetriever.findPlayerByTicketId(ticketId);
    }


}
