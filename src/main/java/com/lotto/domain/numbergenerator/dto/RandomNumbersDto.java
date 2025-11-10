package com.lotto.domain.numbergenerator.dto;

import lombok.Builder;

import java.util.Set;

@Builder
public record RandomNumbersDto(Set<Integer> numbers) {
}
