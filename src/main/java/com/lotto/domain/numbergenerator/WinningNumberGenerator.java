package com.lotto.domain.numbergenerator;

import com.lotto.domain.numbergenerator.dto.RandomNumbersDto;
import com.lotto.domain.numbergenerator.dto.WinningNumbersDto;
import com.lotto.domain.numberreceiver.NumberReceiverFacade;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@RequiredArgsConstructor
class WinningNumberGenerator {

    private final NumberReceiverFacade numberReceiverFacade;
    private final WinningNumberGeneratorConfiguration winningNumbersConfig;
    private final RandomGenerable randomGenerable;
    private final NumberValidator numberValidator;
    private final WinningNumberRepository winningNumberRepository;
    private final RandomNumbersGenerable randomNumbersGenerable;

    WinningNumbersDto generateWinningNumbers(){

        LocalDateTime nextDrawnDate = numberReceiverFacade.retrieveNextDrawnDate();
//        RandomNumbersDto randomNumbersDto = randomGenerable.generateRandomNumbers(winningNumbersConfig.count(),
//                winningNumbersConfig.lowerBand(), winningNumbersConfig.upperBand());
//
//        Set<Integer> resultWinningNumbers = randomNumbersDto.numbers();

        Set<Integer> resultWinningNumbers = randomNumbersGenerable.generateSixRandomNumber();
        numberValidator.validateNumbers(resultWinningNumbers);

        WinningNumbers winningNumbers = saveWinningNumbers(nextDrawnDate, resultWinningNumbers);
        return mapFromWinningNumber(winningNumbers);



    }

    WinningNumbersDto findWinningNumbersByDate(LocalDateTime date){
        WinningNumbers winningNumbers = winningNumberRepository.findByDate(date)
                .orElseThrow(() -> new WinningNumbersNotFoundException("Winning numbers not found!"));

        return  mapFromWinningNumber(winningNumbers);
    }

    boolean areWinningNumbersExistingByDate(){
        LocalDateTime nextDrawnDate = numberReceiverFacade.retrieveNextDrawnDate();
        return winningNumberRepository.existsByDate(nextDrawnDate);
    }

    private WinningNumbers saveWinningNumbers(LocalDateTime nextDrawnDate, Set<Integer> resultWinningNumbers) {
        WinningNumbers builtNumbers = WinningNumbers.builder()
                .date(nextDrawnDate)
                .winningNumbers(resultWinningNumbers)
                .build();

        return winningNumberRepository.save(builtNumbers);

    }

    private WinningNumbersDto mapFromWinningNumber(WinningNumbers winningNumbers) {
        return WinningNumbersDto.builder()
                .date(winningNumbers.date())
                .winningNumbers(winningNumbers.winningNumbers())
                .build();
    }


}
