package com.lotto.domain.loginandregister.dto;


import lombok.Builder;

@Builder
public record UserDto(String id, String email, String password) {
}
