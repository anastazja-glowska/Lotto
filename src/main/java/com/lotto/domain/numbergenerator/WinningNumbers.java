package com.lotto.domain.numbergenerator;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Set;

@Document
@Builder
public record WinningNumbers(@Id String id, Set<Integer> winningNumbers, @Indexed(unique = true)LocalDateTime date) {
}
