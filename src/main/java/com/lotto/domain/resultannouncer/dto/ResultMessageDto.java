package com.lotto.domain.resultannouncer.dto;

import lombok.Builder;

@Builder
public record ResultMessageDto(String message, ResultDto resultDto) {
}
