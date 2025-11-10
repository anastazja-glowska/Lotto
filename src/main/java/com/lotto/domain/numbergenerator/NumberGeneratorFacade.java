package com.lotto.domain.numbergenerator;

import com.lotto.domain.numbergenerator.dto.WinningNumbersDto;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class NumberGeneratorFacade {

    private final WinningNumberGenerator winningNumberGenerator;

    public WinningNumbersDto generateWinningNumbers(){
        return winningNumberGenerator.generateWinningNumbers();
    }

    public WinningNumbersDto retrieveWinningNumbersByDate(LocalDateTime date){
        return winningNumberGenerator.findWinningNumbersByDate(date);
    }

    public boolean areWinningNumbersExistingByDate(){
        return winningNumberGenerator.areWinningNumbersExistingByDate();
    }
}
