package com.lotto.domain.numbergenerator;

import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public interface RandomNumbersGenerable {

    Set<Integer> generateSixRandomNumber();
}
