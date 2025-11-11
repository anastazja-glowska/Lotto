package com.lotto.domain.numbergenerator;

import com.lotto.domain.numberreceiver.NumberReceiverFacade;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class NumberGeneratorFacadeTest {

    private final RandomGenerable randomGenerable = new RandomGenerableTestImpl();
    private final WinningNumberRepository winningNumberRepository = new WinningNumberRepositoryTestImpl();
    NumberReceiverFacade numberReceiverFacade = mock(NumberReceiverFacade.class);
    private final WinningNumberGeneratorConfiguration generatorConfiguration =
            WinningNumberGeneratorConfiguration.builder()
                    .count(6)
                    .lowerBand(1)
                    .upperBand(99)
                    .build();
    private final NumberValidator numberValidator= new NumberValidator();
    private final WinningNumberGenerator winningNumberGenerator =
            new WinningNumberGenerator(numberReceiverFacade, generatorConfiguration, randomGenerable,
                    numberValidator, winningNumberRepository);

    NumberGeneratorFacade numberGeneratorFacade = new NumberGeneratorConfig()
            .numberGeneratorFacade(winningNumberGenerator);


    @Test
    void should_generate_six_random_numbers(){

        // given

        //when

        //then
    }

    @Test
    void should_generate_six_random_numbers_in_correct_range(){

        // given

        //when

        //then
    }

    @Test
    void should_retrieve_winning_numbers_by_date(){

        // given

        //when

        //then
    }

    @Test
    void should_throw_exception_when_winning_numbers_not_found(){

        // given

        //when

        //then
    }


    @Test
    void should_save_winning_numbers_to_winning_numbers_repository(){

        // given

        //when

        //then
    }

}