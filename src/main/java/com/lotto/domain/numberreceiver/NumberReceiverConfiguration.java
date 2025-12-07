package com.lotto.domain.numberreceiver;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class NumberReceiverConfiguration {

    @Bean
    Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    NumberValidator numberValidator() {
        return new NumberValidator();
    }

    @Bean
    DrawDateGenerator drawDateGenerator(Clock clock) {
        return new DrawDateGenerator(clock);
    }

    @Bean
    HashGenerator hashGenerator() {
        return new HashGenerator();
    }

    @Bean
    NumberChecker numberChecker(NumberValidator numberValidator,
                                DrawDateGenerator drawDateGenerator,
                                HashGenerable hashGenerator,
                                TicketRepository ticketRepository) {

        return new NumberChecker(numberValidator, drawDateGenerator, hashGenerator, ticketRepository);

    }

    @Bean
    NumberReceiverFacade numberReceiverFacade(Clock clock, TicketRepository ticketRepository, HashGenerable hashGenerator) {
        DrawDateGenerator drawDateGenerator = new DrawDateGenerator(clock);

        NumberChecker numberChecker = numberChecker(numberValidator(), drawDateGenerator,
                hashGenerator, ticketRepository);


        return new NumberReceiverFacade(clock, numberChecker, drawDateGenerator, ticketRepository);
    }
}
