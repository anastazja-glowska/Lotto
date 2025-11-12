package com.lotto.domain.numbergenerator;

import com.lotto.domain.numbergenerator.dto.RandomNumberResponseDto;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
class RandomNumbersGenerator implements RandomNumbersGenerable{

    private static final Integer LOWER_BAND = 1;
    private static final Integer UPPER_BAND = 99;

    private final RandomOneNumberRetriever remoteClient;

    @Override
    public Set<Integer> generateSixRandomNumber() {

        Set<Integer> generatedWinningNumbers = new HashSet<>();
        while(isNumbersAmountLowerThanSix(generatedWinningNumbers)) {
            RandomNumberResponseDto randomNumberResponseDto = remoteClient.retrieveOneRandomNumber(LOWER_BAND, UPPER_BAND);
            Integer number = randomNumberResponseDto.number();
            generatedWinningNumbers.add(number);
        }

        return generatedWinningNumbers;
    }

    private boolean isNumbersAmountLowerThanSix(Set<Integer> generatedWinningNumbers) {
        return generatedWinningNumbers.size() < 6;
    }
}
