package com.lotto.infrastructure.resultchecker.scheduler;

import com.lotto.domain.numbergenerator.NumberGeneratorFacade;
import com.lotto.domain.numbergenerator.WinningNumbersNotFoundException;
import com.lotto.domain.resultchecker.ResultCheckerFacade;
import com.lotto.domain.resultchecker.dto.AllPlayersDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Log4j2
@RequiredArgsConstructor
public class GenerateWinnersScheduler {

    private final ResultCheckerFacade resultCheckerFacade;
    private final NumberGeneratorFacade numberGeneratorFacade;

    @Scheduled(cron = "${lotto.result-checker.scheduled.run.occurrence}")
    public AllPlayersDto checkAndRetrieveWinners(){
        if(!numberGeneratorFacade.areWinningNumbersExistingByDate()){
            log.error("Winning numbers for this date do not exists");
            throw new WinningNumbersNotFoundException("Winning numbers for this date not found!");
        }
        AllPlayersDto allPlayersDto = resultCheckerFacade.retrieveWinners();
        log.info(String.format("Winners: %s", allPlayersDto));
        return allPlayersDto;
    }
}
