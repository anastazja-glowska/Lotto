package com.lotto.domain.numberreceiver;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
class NumberValidator {


    public static final int MIN_NUMBER_FROM_USER = 1;
    public static final int MAX_NUMBER_FROM_USER = 99;
    public static final int MAX_ALL_NUMBERS_FROM_USER = 6;



    boolean areAllNumbersInRange(Set<Integer> numbersFromUser) {


        return numbersFromUser.stream()
                .filter(number -> number >= MIN_NUMBER_FROM_USER)
                .filter(number -> number <= MAX_NUMBER_FROM_USER)
                .count() == MAX_ALL_NUMBERS_FROM_USER;
    }
}
