package com.lotto.domain.resultannouncer;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
record ResultAnswer(
        @Id String hash,
        Set<Integer> numbers,
        Set<Integer> winningNumbers,
        LocalDateTime drawDate,
        boolean isWinner,
        @Indexed(expireAfterSeconds = 10)
        LocalDateTime timeOfCreation


) {
}
