package com.lotto.infrastructure.numbergenerator.http;

import lombok.Builder;
import org.springframework.http.HttpStatusCode;

@Builder
public record RestTemplateExceptionResponseDto(String message, HttpStatusCode status) {
}
