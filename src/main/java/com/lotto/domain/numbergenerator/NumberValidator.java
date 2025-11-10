package com.lotto.domain.numbergenerator;

import java.util.Set;

class NumberValidator {

    private static final Integer LOWER_BAND = 1;
    private static final Integer UPPER_BAND = 99;

    Set<Integer> validateNumbers(Set<Integer> numbers) {
        if(areNumbersInRange(numbers)){
            throw new NumberOutOfRangeException("Numbers are not in range!");
        }
        return numbers;
    }


    private boolean areNumbersInRange(Set<Integer> numbers) {
        return numbers.stream().anyMatch(number -> number < LOWER_BAND || number > UPPER_BAND);
    }
}
