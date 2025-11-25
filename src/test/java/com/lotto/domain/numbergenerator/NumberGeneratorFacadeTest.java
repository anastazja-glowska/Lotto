package com.lotto.domain.numbergenerator;

import com.lotto.domain.numbergenerator.dto.WinningNumbersDto;
import com.lotto.domain.numberreceiver.NumberReceiverFacade;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Log4j2
class NumberGeneratorFacadeTest {


    private static final LocalDateTime NEXT_DRAWN_DATE = LocalDateTime.of(2025, 11, 15, 12, 0, 0);

    private final RandomGenerable randomGenerable = new RandomGenerableImpl();
    private final WinningNumberRepository winningNumberRepository = new WinningNumberRepositoryTestImpl();
    NumberReceiverFacade numberReceiverFacade = mock(NumberReceiverFacade.class);
    private final WinningNumberGeneratorConfiguration generatorConfiguration =
            WinningNumberGeneratorConfiguration.builder()
                    .count(6)
                    .lowerBand(1)
                    .upperBand(99)
                    .build();


    private final RandomNumbersGenerable randomNumbersGenerator = new SecureRandomGeneratorTestImpl();

    private final NumberValidator numberValidator = new NumberValidator();
    private final WinningNumberGenerator winningNumberGenerator =
            new WinningNumberGenerator(numberReceiverFacade, generatorConfiguration, randomGenerable,
                    numberValidator, winningNumberRepository, randomNumbersGenerator);

    NumberGeneratorFacade numberGeneratorFacade = new NumberGeneratorConfig()
            .numberGeneratorFacade(winningNumberGenerator);


    @Test
    @DisplayName("Should generate six random numbers")
    void should_generate_six_random_numbers() {

        // given
        mockDrawDate();

        //when
        WinningNumbersDto winningNumbersDto = numberGeneratorFacade.generateWinningNumbers();
        Set<Integer> winningNumbers = winningNumbersDto.getWinningNumbers();
        log.info("Winning numbers are: {}", winningNumbers);

        //then
        assertThat(winningNumbers).hasSize(6);


    }

    @Test
    @DisplayName("Should generate six random numbers in correct range")
    void should_generate_six_random_numbers_in_correct_range() {

        // given
        mockDrawDate();
        int lower_band = 1;
        int upper_band = 99;

        //when
        WinningNumbersDto winningNumbersDto = numberGeneratorFacade.generateWinningNumbers();


        //then
        Set<Integer> winningNumbers = winningNumbersDto.getWinningNumbers();

        log.info("Winning numbers are: {}", winningNumbers);
        boolean areNotInCorrectRange = winningNumbers.stream()
                .anyMatch(number -> number < lower_band || number > upper_band);

        assertThat(areNotInCorrectRange).isFalse();

    }

    @Test
    @DisplayName("Should retrieve winning numbers by date")
    void should_retrieve_winning_numbers_by_date() {

        // given
        mockDrawDate();

        WinningNumbersDto generatedWinningNumbers = numberGeneratorFacade.generateWinningNumbers();
        log.info("Generated winning Numbers " + generatedWinningNumbers);
        Set<Integer> generatedNumbers = generatedWinningNumbers.getWinningNumbers();

        //when
        WinningNumbersDto winningNumbersDto = numberGeneratorFacade.retrieveWinningNumbersByDate(NEXT_DRAWN_DATE);


        //then
        assertThat(winningNumbersDto.getDate()).isEqualTo(NEXT_DRAWN_DATE);
        assertThat(winningNumbersDto.getWinningNumbers()).isEqualTo(generatedNumbers);
    }

    @Test
    @DisplayName("Should throw exception when winning numbers not found")
    void should_throw_exception_when_winning_numbers_not_found() {

        // given
        mockDrawDate();

        //when
        Throwable throwable = catchThrowable(() -> numberGeneratorFacade.retrieveWinningNumbersByDate(NEXT_DRAWN_DATE));

        //then
        assertThat(throwable).isInstanceOf(WinningNumbersNotFoundException.class);
        assertThat(throwable.getMessage()).isEqualTo("Winning numbers not found!");
    }


    @Test
    @DisplayName("Should save winning numbers to winning numbers repository")
    void should_save_winning_numbers_to_winning_numbers_repository() {

        // given

        mockDrawDate();

        //when
        WinningNumbersDto generatedWinningNumbers = numberGeneratorFacade.generateWinningNumbers();

        //then
        log.info("Generated winning Numbers " + generatedWinningNumbers);
        Optional<WinningNumbers> optionalWinningNumbers = winningNumberRepository
                .findByDate(generatedWinningNumbers.getDate());

        assertThat(optionalWinningNumbers.isPresent()).isTrue();
        assertThat(optionalWinningNumbers.get().winningNumbers()).isEqualTo(generatedWinningNumbers.getWinningNumbers());
    }


    @Test
    @DisplayName("Should return true if winning numbers exists by date")
    void should_return_true_if_winning_numbers_exists_by_date() {

        // given
        mockDrawDate();
        Set<Integer> winningNumbers = Set.of(1, 2, 3, 4, 7, 8);

        WinningNumbers numbers = WinningNumbers.builder()
                .id("001")
                .date(NEXT_DRAWN_DATE)
                .winningNumbers(winningNumbers)
                .build();

        winningNumberRepository.save(numbers);
        //when
        boolean areNumbersExisting = numberGeneratorFacade.areWinningNumbersExistingByDate();


        //then
        assertThat(areNumbersExisting).isTrue();
    }


    private void mockDrawDate() {
        when(numberReceiverFacade.retrieveNextDrawnDate()).thenReturn(NEXT_DRAWN_DATE);
    }

}