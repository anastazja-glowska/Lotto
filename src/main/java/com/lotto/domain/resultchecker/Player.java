package com.lotto.domain.resultchecker;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
@Document
public record Player(@Id String hash, Set<Integer> numbers, Integer numberOfHits,
              LocalDateTime drawDate, boolean isWinner, Set<Integer> winningNumbers) {
}
