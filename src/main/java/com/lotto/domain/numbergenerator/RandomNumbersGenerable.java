package com.lotto.domain.numbergenerator;

import com.lotto.domain.numbergenerator.dto.SixRandomNumbersDto;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public interface RandomNumbersGenerable {

    SixRandomNumbersDto generateSixRandomNumber();
}
