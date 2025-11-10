package com.lotto.domain.numberreceiver;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
class NumberValidator {


    public static final int MIN_NUMBER_FROM_USER = 1;
    public static final int MAX_NUMBER_FROM_USER = 99;
    public static final int MAX_ALL_NUMBERS_FROM_USER = 6;

    List<ValidationInfo> validationInfos;

    List<ValidationInfo> validateUserNumbers(Set<Integer> userNumbers) {
        validationInfos = new LinkedList<>();

        if(userNumbers.size() != MAX_ALL_NUMBERS_FROM_USER){
            validationInfos.add(ValidationInfo.SIX_NUMBERS_ARE_NOT_PROVIDEN);
        }

        if(!areAllNumbersInRange(userNumbers)){
            validationInfos.add(ValidationInfo.NUMBERS_NOT_IN_RANGE);
        }

        return validationInfos;



    }

    String allResultMessage(){
        return this.validationInfos.stream()
                .map(validationInfo -> validationInfo.message)
                .collect(Collectors.joining(","));
    }



    boolean areAllNumbersInRange(Set<Integer> numbersFromUser) {


        return numbersFromUser.stream()
                .filter(number -> number >= MIN_NUMBER_FROM_USER)
                .filter(number -> number <= MAX_NUMBER_FROM_USER)
                .count() == MAX_ALL_NUMBERS_FROM_USER;
    }
}
