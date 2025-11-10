package com.lotto.domain.numbergenerator;

import com.lotto.domain.numbergenerator.dto.RandomNumbersDto;
import org.springframework.stereotype.Component;

@Component
public interface RandomGenerable {

    RandomNumbersDto generateRandomNumbers(int count, int lowerBand, int upperBand);
}
