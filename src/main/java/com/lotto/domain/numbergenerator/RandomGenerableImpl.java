package com.lotto.domain.numbergenerator;

import com.lotto.domain.numbergenerator.dto.RandomNumbersDto;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class RandomGenerableImpl implements RandomGenerable{

    private final Set<Integer> generatedNumbers;

    RandomGenerableImpl(){
        generatedNumbers = Set.of(1, 2, 3, 4, 5,6 );
    }

    RandomGenerableImpl(Set<Integer> generatedNumbers){
        this.generatedNumbers = generatedNumbers;
    }


    @Override
    public RandomNumbersDto generateRandomNumbers(int count, int lowerBand, int upperBand) {

        return new RandomNumbersDto(generatedNumbers);
    }
}
