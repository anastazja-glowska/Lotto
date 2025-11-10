package com.lotto.domain.numbergenerator;


import lombok.Builder;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Builder
@ConfigurationProperties(prefix = "lotto.winning-numbers")
public record WinningNumberGeneratorConfiguration(int count, int lowerBand, int upperBand) {
}
