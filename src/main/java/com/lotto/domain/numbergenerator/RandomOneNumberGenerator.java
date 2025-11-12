package com.lotto.domain.numbergenerator;

import com.lotto.domain.numbergenerator.dto.RandomNumberResponseDto;

import java.security.SecureRandom;
import java.util.Random;

class RandomOneNumberGenerator implements RandomOneNumberRetriever {

    @Override
    public RandomNumberResponseDto retrieveOneRandomNumber(int lowerBand, int upperBand) {
        Random random = new SecureRandom();
        int randomNumber = random.nextInt((upperBand - lowerBand) + 1);
        return new RandomNumberResponseDto(randomNumber);
    }
}
