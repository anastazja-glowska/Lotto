package com.lotto.infrastructure.numbergenerator.scheduler;

import com.lotto.domain.numbergenerator.NumberGeneratorFacade;
import com.lotto.domain.numbergenerator.dto.WinningNumbersDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class WinningNumberScheduler {

    private final NumberGeneratorFacade  numberGeneratorFacade;

    @Scheduled(cron = "${lotto.winning-numbers.scheduled.run.occurrence}")
    public  void scheduleWinningNumbers(){
        WinningNumbersDto winningNumbers = numberGeneratorFacade.generateWinningNumbers();
        log.info("WinningNumbers: " + winningNumbers.getWinningNumbers());
        log.info("Date " +  winningNumbers.getDate());
    }
}
