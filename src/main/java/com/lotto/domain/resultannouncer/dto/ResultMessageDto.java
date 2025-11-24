package com.lotto.domain.resultannouncer.dto;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record ResultMessageDto(String message, ResultDto resultDto) implements Serializable {
}
