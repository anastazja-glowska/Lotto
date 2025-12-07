package com.lotto.domain.resultchecker;

import com.lotto.domain.numbergenerator.NumberGeneratorFacade;
import com.lotto.domain.numberreceiver.NumberReceiverFacade;
import lombok.Builder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ResultCheckerConfiguration {

    @Bean
    ResultRetriever resultRetriever(NumberReceiverFacade numberReceiverFacade, NumberGeneratorFacade numberGeneratorFacade,
                                    PlayersRepository playersRepository, WinnersEvaluator winnersEvaluator) {

        return new ResultRetriever(numberReceiverFacade, numberGeneratorFacade,
                winnersEvaluator, playersRepository);

    }

    @Bean
    ResultCheckerFacade resultCheckerFacade(ResultRetriever resultRetriever) {
        return new ResultCheckerFacade(resultRetriever);
    }
}
