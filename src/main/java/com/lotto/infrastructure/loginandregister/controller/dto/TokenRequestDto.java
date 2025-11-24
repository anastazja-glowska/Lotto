package com.lotto.infrastructure.loginandregister.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record TokenRequestDto(
        @NotBlank(message = "{email.not.blank}")
        String email,

        @NotBlank(message = "{password.not.blank}")
        String password) {
}
