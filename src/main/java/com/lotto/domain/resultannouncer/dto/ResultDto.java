package com.lotto.domain.resultannouncer.dto;

import lombok.Builder;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record ResultDto(String hash, Set<Integer> numbers,
                        LocalDateTime drawDate, boolean isWinner, Set<Integer> winningNumbers) {
}
