package com.lotto.domain.numbergenerator;


import com.lotto.domain.numbergenerator.dto.SixRandomNumbersDto;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Component
@Primary
class SecureRandomGeneratorTestImpl implements RandomNumbersGenerable{

    private static final Integer LOWER_BAND = 1;
    private static final Integer UPPER_BAND = 99;


    @Override
    public SixRandomNumbersDto generateSixRandomNumber() {

        Set<Integer> generatedWinningNumbers = new HashSet<>();
        while(isNumbersAmountLowerThanSix(generatedWinningNumbers)) {

            Random random = new SecureRandom();
            int randomNumber = random.nextInt((UPPER_BAND - LOWER_BAND) + 1);
            generatedWinningNumbers.add(randomNumber);
        }

        return SixRandomNumbersDto.builder()
                .numbers(generatedWinningNumbers)
                .build();
    }

    private boolean isNumbersAmountLowerThanSix(Set<Integer> generatedWinningNumbers) {
        return generatedWinningNumbers.size() < 6;
    }
}
