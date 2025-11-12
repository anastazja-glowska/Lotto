package com.lotto.domain.numbergenerator;

import com.lotto.domain.numbergenerator.dto.RandomNumberResponseDto;

public interface RandomOneNumberRetriever {

    RandomNumberResponseDto retrieveOneRandomNumber(int lowerBand, int upperBand);
}
