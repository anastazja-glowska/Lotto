package com.lotto.domain.numbergenerator;

import com.lotto.domain.numbergenerator.dto.RandomNumbersDto;

import java.util.Set;

class RandomGenerableTestImpl implements RandomGenerable{

    private final Set<Integer> generatedNumbers;

    RandomGenerableTestImpl(){
        generatedNumbers = Set.of(1, 2, 3, 4, 5,6 );
    }

    RandomGenerableTestImpl(Set<Integer> generatedNumbers){
        this.generatedNumbers = generatedNumbers;
    }


    @Override
    public RandomNumbersDto generateRandomNumbers(int count, int lowerBand, int upperBand) {

        return new RandomNumbersDto(generatedNumbers);
    }
}
