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
    NumberReceiverFacade numberReceiverFacade(Clock clock, TicketRepository ticketRepository, NumberChecker numberChecker) {
        DrawDateGenerator drawDateGenerator = new DrawDateGenerator(clock);
        return new NumberReceiverFacade(clock, numberChecker, drawDateGenerator, ticketRepository);
    }
}
