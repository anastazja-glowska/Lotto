package com.lotto.domain.resultannouncer;

import com.lotto.domain.resultchecker.ResultCheckerFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
class ResultAnnouncerConfiguration {

    @Bean
    ResultAnnounceRetriever resultAnnounceRetriever(Clock clock, ResultRepository resultRepository,
                                                    ResultCheckerFacade resultCheckerFacade) {

        return new ResultAnnounceRetriever(resultRepository, resultCheckerFacade, clock);

    }

    @Bean
    ResultAnnouncerFacade resultAnnouncerFacade(ResultAnnounceRetriever resultAnnounceRetriever) {
        return new ResultAnnouncerFacade(resultAnnounceRetriever);
    }
}
