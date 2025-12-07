package com.lotto.domain.numbergenerator;

import com.lotto.domain.numberreceiver.NumberReceiverFacade;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
@EnableConfigurationProperties(WinningNumberGeneratorConfiguration.class)
class NumberGeneratorConfig {


    @Bean
    WinningNumberGenerator winningNumberGenerator(NumberReceiverFacade numberReceiverFacade,
                                                  RandomGenerable randomGenerable, WinningNumberRepository winningNumberRepository,
                                                  WinningNumberGeneratorConfiguration winningNumberGeneratorConfiguration,
                                                  RandomNumbersGenerable randomNumbersGenerable) {
        NumberValidator numberValidator = new NumberValidator();

        return new WinningNumberGenerator(numberReceiverFacade, winningNumberGeneratorConfiguration,
                randomGenerable, numberValidator, winningNumberRepository, randomNumbersGenerable);

    }


    @Bean
    NumberGeneratorFacade numberGeneratorFacade(WinningNumberGenerator winningNumberGenerator) {

        return new NumberGeneratorFacade(winningNumberGenerator);
    }
}
